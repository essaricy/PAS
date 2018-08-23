package com.softvision.digital.pms.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.auth.repo.SVLdapRepository;
import com.softvision.digital.pms.auth.repo.SVProjectRepository;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.mapper.EmployeeMapper;
import com.softvision.digital.pms.employee.model.SVEmployee;
import com.softvision.digital.pms.employee.repo.EmployeeDataRepository;
import com.softvision.digital.pms.role.constant.Roles;
import com.softvision.digital.pms.role.entity.Role;
import com.softvision.digital.pms.role.repo.RoleDataRepository;
import com.softvision.digital.pms.user.model.User;

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

	public User authenticate(String userid, String password) {
		try {
		    if (!turnOffLdapAUth) {
		        svLdapRepository.authenticate(userid, password);
		    }
			Optional<Employee> employeeOptional = employeeRepository.findByLoginIdIgnoreCase(userid);
			Employee employee = employeeOptional.orElseGet(() -> {
				// Employee Details does not exist in the system. add them
				// Employee does not exist in the system. Look up SV project.
				Optional<SVEmployee> svEmployeeOptional = svProjectRepository.getEmployee(userid);
				SVEmployee svEmployee = svEmployeeOptional.orElseThrow(() -> new DisabledException("Your account is not available yet"));

				Employee employeeToSave = employeeMapper.getEmployee(svEmployee);
				employeeToSave.setLoginId(svEmployee.getLoginId().toLowerCase());
				employeeToSave.setFirstName(svEmployee.getFirstName().trim());
				employeeToSave.setLastName(svEmployee.getLastName().trim());
				employeeToSave.setActive("Y");
				return employeeRepository.save(employeeToSave);
			});

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
		} catch (javax.naming.AuthenticationException authenticationException) {
			log.error("javax.naming.AuthenticationException for {}, ERROR{}=", userid, authenticationException.getMessage());
            throw new AuthenticationServiceException("Invalid username or password", authenticationException);
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
