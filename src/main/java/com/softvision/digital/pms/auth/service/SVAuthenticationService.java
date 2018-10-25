package com.softvision.digital.pms.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.auth.model.User;
import com.softvision.digital.pms.auth.repo.SVLdapRepository;
import com.softvision.digital.pms.auth.repo.SVProjectRepository;
import com.softvision.digital.pms.common.beans.AppSecurityConfig;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.mapper.EmployeeMapper;
import com.softvision.digital.pms.employee.model.SVEmployee;
import com.softvision.digital.pms.employee.repo.EmployeeDataRepository;
import com.softvision.digital.pms.role.constant.Roles;
import com.softvision.digital.pms.role.entity.Role;
import com.softvision.digital.pms.role.repo.RoleDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SVAuthenticationService {

	@Autowired private SVLdapRepository svLdapRepository;

	@Autowired private SVProjectRepository svProjectRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private RoleDataRepository roleDataRepository;

	@Autowired private EmployeeMapper employeeMapper;

	@Autowired private AppSecurityConfig appSecurityConfig;

    private boolean turnOffLdapAUth;

    @PostConstruct
    private void init() {
    	String mode = appSecurityConfig.getMode();
        turnOffLdapAUth = mode != null && mode.trim().equalsIgnoreCase("disable");
    }

	public User authenticate(String userid, String password) {
		try {
		    if (!turnOffLdapAUth) {
		        svLdapRepository.authenticate(userid, password);
		    }
			Employee employee = employeeRepository.findByLoginIdIgnoreCase(userid);
			// Employee Details does not exist in the system. add them
			if (employee == null) {
				// Employee does not exist in the system. Look up SV project.
				SVEmployee svEmployee = svProjectRepository.getEmployee(userid);
				if (svEmployee == null) {
					throw new DisabledException("Invalid credentials");
				}
				employee = employeeMapper.getEmployee(svEmployee);
				employee.setLoginId(employee.getLoginId().toLowerCase());
				employee.setFirstName(employee.getFirstName().trim());
				employee.setLastName(employee.getLastName().trim());
				employee.setActive("Y");
				employeeRepository.save(employee);
			}
			List<Role> roles = roleDataRepository.findByEmployeeId(employee.getEmployeeId());
			if (roles == null) {
				roles = new ArrayList<>();
			}
			if (!contain(roles, Roles.EMPLOYEE)) {
				roles.add(roleDataRepository.findByRoleName(Roles.EMPLOYEE.getName()));
			}
			return employeeMapper.getUser(employee, roles);
		} catch (AuthenticationNotSupportedException authenticationNotSupportedException) {
            log.error("AuthenticationNotSupportedException for {}, ERROR={}", userid,
                    authenticationNotSupportedException.getMessage());
			throw new AuthenticationServiceException(authenticationNotSupportedException.getMessage(),
					authenticationNotSupportedException);
		} catch (CommunicationException communicationException) {
			log.error("CommunicationException for {}, ERROR={}", userid, communicationException.getMessage());
            throw new AuthenticationServiceException("Unable to connect to Active Directory", communicationException);
		} catch (AuthenticationException authenticationException) {
			log.error("javax.naming.AuthenticationException for {}, ERROR{}=", userid, authenticationException.getMessage());
            throw new AuthenticationServiceException("Invalid username or password", authenticationException);
		} catch (NamingException namingException) {
			log.error("NamingException for {}, ERROR={}", userid, namingException.getMessage());
			throw new BadCredentialsException("Error: " + namingException.getMessage(), namingException);
		} catch (ConstraintViolationException constraintViolationException) {
            log.error("ConstraintViolationException for {}, ERROR={}", userid,
                    constraintViolationException.getMessage());
            throw new AuthenticationServiceException("Something went wrong", constraintViolationException);
        } catch (Exception exception) {
		    log.error("Exception for {}, ERROR={}", userid, exception.getMessage());
			String message = exception.getMessage();
			if (message.contains("Connection timed out")) {
				throw new AuthenticationServiceException("Unable to connect to svProject at this moment", exception);
			}
			throw new AuthenticationServiceException(exception.getMessage(), exception);
		}
	}

	private boolean contain(List<Role> roles, Roles employee) {
		for (Role role : roles) {
			if (role.getRoleName().equalsIgnoreCase(employee.getName())) {
				return true;
			}
		}
		return false;
	}

}
