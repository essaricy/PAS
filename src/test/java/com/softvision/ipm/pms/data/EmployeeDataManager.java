package com.softvision.ipm.pms.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.service.EmployeeService;

@Component
public class EmployeeDataManager implements AbstractDataManager {

	@Autowired EmployeeService employeeService;

	@Override
	public void clearData() throws Exception {
		List<Employee> employees = employeeService.getEmployees();
		if (employees != null && !employees.isEmpty()) {
			for (Employee employee : employees) {
				employeeService.delete(employee.getEmployeeId());
			}
		}
	}

	@Override
	public void loadData() throws Exception {
		List<Result> syncEmployees = employeeService.syncEmployees();
		for (Result syncEmployee : syncEmployees) {
			System.out.println(syncEmployee);
		}
	}

}
