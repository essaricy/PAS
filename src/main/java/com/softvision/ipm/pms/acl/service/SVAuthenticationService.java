package com.softvision.ipm.pms.acl.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.acl.repo.SVLdapRepository;
import com.softvision.ipm.pms.acl.repo.SVProjectRepository;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.mapper.EmployeeMapper;
import com.softvision.ipm.pms.employee.model.SVEmployee;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.user.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SVAuthenticationService {

	@Autowired private SVLdapRepository svLdapRepository;

	@Autowired private SVProjectRepository svProjectRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private RoleDataRepository roleDataRepository;

	@Autowired private EmployeeMapper employeeMapper;

    @Value("${app.security.ldap.mode}")
    private String mode;

    private boolean turnOffLdapAUth;

    @PostConstruct
    private void init() {
        turnOffLdapAUth = mode != null && mode.trim().equalsIgnoreCase("disable");
    }

	public User authenticate(String userid, String password) throws AuthenticationException {
		try {
		    if (!turnOffLdapAUth) {
		        svLdapRepository.authenticate(userid, password);
		    }
			Employee employee = employeeRepository.findByLoginId(userid);
			// Employee Details does not exist in the system. add them
			if (employee == null) {
				// Employee does not exist in the system. Look up SV project.
				SVEmployee svEmployee = svProjectRepository.getEmployee(userid);
				if (svEmployee == null) {
					throw new DisabledException("Invalid credentials");
				}
				employeeRepository.save(employeeMapper.getEmployee(svEmployee));
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
		} catch (NamingException namingException) {
			if (namingException instanceof CommunicationException) {
			    log.error("CommunicationException for {}, ERROR={}", userid, namingException.getMessage());
                throw new AuthenticationServiceException("Unable to connect to Active Directory", namingException);
            } else if (namingException instanceof javax.naming.AuthenticationException) {
                log.error("javax.naming.AuthenticationException for {}, ERROR{}=", userid, namingException.getMessage());
                throw new AuthenticationServiceException("Invalid username or password", namingException);
            }
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
			if (role.getRoleName().equalsIgnoreCase(Roles.EMPLOYEE.getName())) {
				return true;
			}
		}
		return false;
	}

}
