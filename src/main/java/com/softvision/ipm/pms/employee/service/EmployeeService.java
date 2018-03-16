package com.softvision.ipm.pms.employee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.acl.repo.SVProjectRepository;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.CollectionUtil;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.employee.repo.EmployeeSpecs;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private SVProjectRepository svProjectRepository;

	public List<Employee> getEmployees() {
		return CollectionUtil.toList(employeeRepository.findAll());
	}

	public Employee getEmployee(int employeeId) {
		return employeeRepository.findByEmployeeId(employeeId);
	}

	public Employee getEmployee(String loginId) {
		return employeeRepository.findByLoginId(loginId);
	}

	public List<Employee> search(String searchString) {
		if (searchString != null && searchString.trim().length() > 2) {
			return employeeRepository.findAll(EmployeeSpecs.searchInName(searchString));
		}
		return null;
	}

	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee save(String loginId) {
		Employee employee = svProjectRepository.getEmployee(loginId);
		if (employee != null) {
			return employeeRepository.save(employee);
		}
		return null;
	}

	public List<String> getEmployeeTypes() {
		return employeeRepository.findEmployeeTypes();
	}

	public List<Employee> roleSearch(String searchString) {
		if (searchString != null) {
			return employeeRepository.findEmployeesByRoleName(searchString	);
		}
		return null;
	}

	public List<Result> syncEmployees() throws Exception {
		List<Result> resultList = new ArrayList<Result>();
		List<Employee> employeeList = svProjectRepository.getAllEmployees();
		for (Employee employee : employeeList) {
			Result result= new Result();
			try{
				save(employee);
				result.setCode(Result.SUCCESS);
				result.setMessage(employee.getFirstName() + " " + employee.getLastName() + " updated");
			}catch (Exception e) {
				result.setCode(Result.FAILURE);
				result.setMessage(employee.getFirstName() + " " + employee.getLastName() + " update failed. Error = "+ e.getMessage());
			}
			resultList.add(result);
		}
		return resultList;
	}
}
