package com.softvision.ipm.pms.acl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.constants.Roles;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.user.model.User;

@Service
public class SVDevAuthenticationService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RoleDataRepository roleDataRepository;

	public User authenticate(String userid, String password) throws AuthenticationException {
		try {
			Employee employee = employeeRepository.findByLoginId(userid);
			List<Role> roles = roleDataRepository.findByEmployeeId(employee.getEmployeeId());
			if (roles == null) {
				roles = new ArrayList<>();
			}
			if (!contain(roles, Roles.EMPLOYEE)) {
				roles.add(roleDataRepository.findByRoleName(Roles.EMPLOYEE.getName()));
			}
			return getUser(employee, roles);
		} catch (Exception exception) {
			String message = exception.getMessage();
			if (message.contains("Connection timed out")) {
				throw new AuthenticationServiceException("Unable to connect to svProject at this moment", exception);
			}
			throw new AuthenticationServiceException(exception.getMessage(), exception);
		}
	}

	private User getUser(Employee employee, List<Role> roles) {
		User user = null;
		if (employee != null) {
			user = new User();
			user.setBand(employee.getBand());
			user.setDesignation(employee.getDesignation());
			user.setEmployeeId(employee.getEmployeeId());
			user.setFirstName(employee.getFirstName());
			user.setJoinedDate(employee.getHiredOn());
			user.setLastName(employee.getLastName());
			user.setLocation(employee.getLocation());
			user.setUsername(employee.getLoginId());
			user.setRoles(roles);
		}
		return user;
	}

	private boolean contain(List<Role> roles, Roles employee) {
		for (Role role : roles) {
			if (role.getRoleName().equalsIgnoreCase(Roles.EMPLOYEE.getName())) {
				return true;
			}
		}
		return false;
	}
}
