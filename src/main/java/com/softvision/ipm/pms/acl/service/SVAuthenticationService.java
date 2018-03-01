package com.softvision.ipm.pms.acl.service;

import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.acl.repo.SVLdapRepository;
import com.softvision.ipm.pms.acl.repo.SVProjectRepository;
import com.softvision.ipm.pms.common.constants.Roles;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.user.model.User;

@Service
public class SVAuthenticationService {

	@Autowired
	private SVLdapRepository svLdapRepository;

	@Autowired
	private SVProjectRepository svProjectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RoleDataRepository roleDataRepository;

	public User authenticate(String userid, String password) throws AuthenticationException {
		try {
			//svLdapRepository.authenticate(userid, password);
			Employee employee = svProjectRepository.getEmployee(userid);
			System.out.println("employee=" + employee);
			Employee findByLoginId = employeeRepository.findByLoginId(employee.getLoginId());
			// Employee Details does not exist in the system. add them
			if (findByLoginId == null) {
				employeeRepository.save(employee);
			}
			List<Role> roles = roleDataRepository.findByEmployeeId(Long.valueOf(employee.getEmployeeId()));
			
			boolean flag = true;
			if (roles != null && !roles.isEmpty()) {
				for (Role role : roles) {
					if (role.getRoleName().equalsIgnoreCase(Roles.EMPLOYEE.getName())) {
						flag = false;
						break;
					}
				}
			} else if (roles == null) {
					roles = new ArrayList<>();
			}
			if(flag){
				Role empRole = roleDataRepository.findByRoleName(Roles.EMPLOYEE.getName());	
				roles.add(empRole);
			}
			return getUser(employee, roles);
		} /*catch (AuthenticationNotSupportedException authenticationNotSupportedException) {
			throw new AuthenticationServiceException(authenticationNotSupportedException.getMessage(),
					authenticationNotSupportedException);
		} catch (NamingException namingException) {
			if (namingException instanceof AuthenticationNotSupportedException) {
				throw new AuthenticationServiceException(namingException.getMessage(), namingException);
			} else if (namingException instanceof CommunicationException) {
				throw new AuthenticationServiceException("Unable to connect to Active Directory", namingException);
			}
			namingException.printStackTrace();
			throw new BadCredentialsException("Error: " + namingException.getMessage(), namingException);
		}*/ catch (Exception exception) {
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

}
