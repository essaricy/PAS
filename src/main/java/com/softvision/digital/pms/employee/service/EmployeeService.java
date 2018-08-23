package com.softvision.digital.pms.employee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.auth.repo.SVProjectRepository;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.mapper.EmployeeMapper;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.employee.model.SVEmployee;
import com.softvision.digital.pms.employee.repo.EmployeeDataRepository;
import com.softvision.digital.pms.employee.repo.EmployeeSpecs;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {

	private static final List<EmployeeDto> EMPLOYEES_LIST = new ArrayList<>();

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private SVProjectRepository svProjectRepository;

	@Autowired private EmployeeMapper employeeMapper;

	public List<EmployeeDto> getEmployees() {
		return employeeMapper.getEmployeeDtoList(employeeRepository.findAll());
	}

	public EmployeeDto getEmployee(int employeeId) {
		return employeeMapper.getEmployeeDto(employeeRepository.findByEmployeeId(employeeId).orElse(null));
	}

	public EmployeeDto getEmployee(String loginId) {
		return employeeMapper.getEmployeeDto(employeeRepository.findByLoginIdIgnoreCase(loginId).orElse(null));
	}

	public List<EmployeeDto> search(String searchString) {
		if (searchString != null && StringUtils.isNotBlank(searchString)
				&&  (Pattern.matches("\\d+", searchString) || searchString.trim().length() > 2)) {
			return employeeMapper.getEmployeeDtoList(employeeRepository.findAll(EmployeeSpecs.searchInName(searchString)));
		}
		return EMPLOYEES_LIST;
	}

	public void save(String loginId) throws ServiceException {
		Optional<SVEmployee> svEmployeeOptional = svProjectRepository.getEmployee(loginId);
		SVEmployee svEmployee = svEmployeeOptional.orElseThrow(() -> new ServiceException("Cannot update the employee as it was not found in SVProject."));
		Employee employee = employeeMapper.getEmployee(svEmployee);
		save(employee);
	}

	public List<String> getEmployeeTypes() {
		return employeeRepository.findEmployeeTypes();
	}

	public List<EmployeeDto> roleSearch(String searchString) {
		if (searchString != null) {
			return employeeMapper.getEmployeeDtoList(employeeRepository.findEmployeesByRoleName(searchString));
		}
		return EMPLOYEES_LIST;
	}

	public List<Result> syncEmployees() {
	    log.info("syncEmployees: START");
		List<Result> resultList = new ArrayList<>();
		List<SVEmployee> svEmployees = svProjectRepository.getAllEmployees();

		// Add New employees and update existing employees
		for (SVEmployee svEmployee : svEmployees) {
			Result result= new Result();
			Employee employee = employeeMapper.getEmployee(svEmployee);
			String employeeName = employee.getFirstName() + " " + employee.getLastName();
			try{
				employee.setActive("Y");
				save(employee);
				result.setCode(Result.SUCCESS);
				result.setMessage(employeeName + " - Updated successfully");
				log.info("syncEmployees: sync completed: {} ", svEmployee.getLoginId());
			} catch (Exception exception) {
				String message = employeeName + " - Update failed. Error = "+ exception.getMessage();
				log.error("syncEmployees: sync failed: {} ERROR=", svEmployee.getLoginId(), exception.getMessage());
				result.setCode(Result.FAILURE);
				result.setMessage(message);
			}
			resultList.add(result);
		}
		// Deactivate exited employees
		List<Employee> existingEmployees = employeeRepository.findAll();
		List<Employee> deactivatedEmployees=existingEmployees.stream().filter(
		        existingEmployee -> {
		            boolean active=false;
		            for (SVEmployee svEmployee : svEmployees) {
                        if (existingEmployee.getEmployeeId() == svEmployee.getEmployeeId()) {
                            active=true;
                            break;
                        }
                    }
		            if (!active) {
		                log.info("Employee " + existingEmployee.getFirstName() + " will be deactivated");
		            }
		            return !active;
		        }
		).collect(Collectors.toList());

		if (deactivatedEmployees != null && !deactivatedEmployees.isEmpty()) {
			for (Employee employee : deactivatedEmployees) {
				Result result= new Result();
				String employeeName = employee.getFirstName() + " " + employee.getLastName();
				try {
					employee.setActive("N");
					save(employee);
					result.setCode(Result.SUCCESS);
					result.setMessage(employeeName + " - Deactivated successfully");
					log.info("syncEmployees: sync deactivated: {} ", employee.getLoginId());
				} catch (Exception exception) {
					String message = employeeName + " - Deactivate failed. Error = "+ exception.getMessage();
					log.error("syncEmployees: sync deactivate failed: {} ERROR=", employee.getLoginId(), exception.getMessage());
					result.setCode(Result.FAILURE);
					result.setMessage(message);
				}
				resultList.add(result);
			}
		}
		log.info("syncEmployees: END");
		return resultList;
	}

	public void delete(int employeeId) {
		employeeRepository.deleteById(employeeId);
	}

	private void save(Employee employee) throws ServiceException {
		if (employee == null) {
			throw new ServiceException("employee information is not provided");
		}
		employeeRepository.save(employee);
	}

}
