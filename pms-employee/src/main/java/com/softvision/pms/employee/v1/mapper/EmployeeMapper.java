package com.softvision.pms.employee.v1.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.pms.employee.v1.entity.Employee;
import com.softvision.pms.employee.v1.model.EmployeeDto;
import com.softvision.pms.employee.v1.model.SVEmployee;

@Component
public class EmployeeMapper {

	@Autowired private ModelMapper mapper;

	public EmployeeDto getEmployeeDto(Employee employee) {
		EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
		if (employeeDto != null) {
			employeeDto.setFullName(employee.getFirstName() + " " + employee.getLastName());
		}
		return employeeDto;
	}

	public List<EmployeeDto> getEmployeeDtoList(List<Employee> all) {
		List<EmployeeDto> employeeDtoList = mapper.map(all, new TypeToken<List<EmployeeDto>>() {}.getType());
		if (employeeDtoList != null && !employeeDtoList.isEmpty()) {
			for (EmployeeDto employeeDto : employeeDtoList) {
				employeeDto.setFullName(employeeDto.getFirstName() + " " + employeeDto.getLastName());
			}
		}
		return employeeDtoList;
	}

	public Employee getEmployee(SVEmployee svEmployee) {
		return mapper.map(svEmployee, Employee.class);
	}

}
