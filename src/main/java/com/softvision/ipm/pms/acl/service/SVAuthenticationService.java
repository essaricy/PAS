package com.softvision.ipm.pms.acl.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.user.model.User;

@Service
public class SVAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SVAuthenticationService.class);

	@Autowired
	private SVLdapRepository svLdapRepository;

	@Autowired
	private SVProjectRepository svProjectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RoleDataRepository roleDataRepository;

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
				employee = svProjectRepository.getEmployee(userid);
				if (employee == null) {
					throw new DisabledException("Invalid credentials");
				}
				employeeRepository.save(employee);
			}
			List<Role> roles = roleDataRepository.findByEmployeeId(employee.getEmployeeId());
			if (roles == null) {
				roles = new ArrayList<>();
			}
			if (!contain(roles, Roles.EMPLOYEE)) {
				roles.add(roleDataRepository.findByRoleName(Roles.EMPLOYEE.getName()));
			}
			return getUser(employee, roles);
		} catch (AuthenticationNotSupportedException authenticationNotSupportedException) {
            LOGGER.error("AuthenticationNotSupportedException for " + userid + ", ERROR="
                    + authenticationNotSupportedException.getMessage());
			throw new AuthenticationServiceException(authenticationNotSupportedException.getMessage(),
					authenticationNotSupportedException);
		} catch (NamingException namingException) {
			if (namingException instanceof CommunicationException) {
			    LOGGER.error("CommunicationException for " + userid + ", ERROR=" + namingException.getMessage());
                throw new AuthenticationServiceException("Unable to connect to Active Directory", namingException);
            } else if (namingException instanceof javax.naming.AuthenticationException) {
                LOGGER.error("javax.naming.AuthenticationException for " + userid + ", ERROR=" + namingException.getMessage());
                throw new AuthenticationServiceException("Invalid username or password", namingException);
            }
			LOGGER.error("NamingException for " + userid + ", ERROR=" + namingException.getMessage());
			throw new BadCredentialsException("Error: " + namingException.getMessage(), namingException);
		} catch (Exception exception) {
		    LOGGER.error("Exception for " + userid + ", ERROR=" + exception.getMessage());
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
			user.setImageUrl("https://opera.softvision.com/Content/Core/img/Profile/" + employee.getEmployeeId() + ".jpg");
			user.setRoles(roles);
		}
		return user;
	}

}
