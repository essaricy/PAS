package com.softvision.ipm.pms.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.service.EmployeeService;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.role.service.RoleService;

@Component
public class EmployeeDataManager implements AbstractDataManager {

	@Autowired EmployeeService employeeService;

	@Autowired RoleService roleService;

	private List<Integer> ADMIN_EMPLOYEES = new ArrayList<>();

	@PostConstruct
	private void init() {
		ADMIN_EMPLOYEES.add(1136);
	}

	@Override
	public void clearData() throws Exception {
		clearEmployeeRoles();
		clearEmployees();
	}

	private void clearEmployeeRoles() {
		List<Employee> managers = employeeService.roleSearch(Roles.MANAGER.getName());
		for (Employee manager : managers) {
			roleService.removeRole(manager.getEmployeeId(), Roles.MANAGER.getCode());
		}
		List<Employee> admins = employeeService.roleSearch(Roles.ADMIN.getName());
		for (Employee admin : admins) {
			roleService.removeRole(admin.getEmployeeId(), Roles.ADMIN.getCode());
		}
	}

	private void clearEmployees() {
		List<Employee> employees = employeeService.getEmployees();
		if (employees != null && !employees.isEmpty()) {
			for (Employee employee : employees) {
				employeeService.delete(employee.getEmployeeId());
			}
		}
	}

	@Override
	public void loadData() throws Exception {
		loadEmployees();
		loadEmployeeRoles();
	}

	private void loadEmployees() throws Exception {
		List<Result> syncEmployees = employeeService.syncEmployees();
		for (Result syncEmployee : syncEmployees) {
			System.out.println(syncEmployee);
		}
	}

	private void loadEmployeeRoles() {
		// Change Roles
		List<Employee> employees = employeeService.getEmployees();
		for (Employee employee : employees) {
			int employeeId = employee.getEmployeeId();
			String band = employee.getBand();
			if (isManager(band)) {
				roleService.assignRole(employeeId, Roles.MANAGER.getCode());
			}
			if (ADMIN_EMPLOYEES.contains(employeeId)) {
				roleService.assignRole(employeeId, Roles.ADMIN.getCode());
			}
		}
	}

	private boolean isManager(String band) {
		if (band != null && band.trim().length() != 0) {
			boolean matches = Pattern.matches(".*(5|6|7|8)(Y|Z)", band);
			return matches;
		}
		return false;
	}

}
