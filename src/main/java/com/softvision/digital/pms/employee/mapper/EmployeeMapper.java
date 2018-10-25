package com.softvision.digital.pms.employee.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.auth.model.User;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.employee.model.SVEmployee;
import com.softvision.digital.pms.role.entity.Role;

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

	public User getUser(Employee employee, List<Role> roles) {
		User user = mapper.map(employee, User.class);
		if (user != null) {
			user.setImageUrl("https://opera.softvision.com/Content/Core/img/Profile/" + employee.getEmployeeId() + ".jpg");
			user.setRoles(roles);
		}
		return user;
	}

}
