package com.softvision.ipm.pms.acl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.constants.Roles;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.user.model.User;
import com.softvision.ipm.pms.user.model.UserToken;

@Service
public class TestAuthenticationProvider implements AuthenticationProvider {

	private static List<User> users = new ArrayList<User>();

	@Autowired
	private RoleDataRepository roleDataRepository;

	@PostConstruct
	private void init() {
		for (int i = 1; i <= 7; i++) {
			User user = new User();
			user.setBand(i + "Z");
			user.setUsername(i + "Z@softvision.com");
			user.setEmployeeId(i);
			user.setUsername("" + i);
			user.setPassword("a");
			user.setDesignation("Designation " + i);
			user.setFirstName("First Name");
			user.setLastName("Last Name");
			// user.setJoinedDate(new SimpleDateFormat("dd/MM/yyyy").format(new
			// Date()));
			user.setJoinedDate(new Date());
			user.setLocation("Location");
			users.add(user);
		}

		User user = new User();
		user.setBand("5Z");
		user.setUsername("srikanth.kumar@abc.com");
		user.setEmployeeId(1136);
		user.setUsername("srikanth.kumar");
		user.setPassword("$oftvision@123");
		user.setDesignation("Designation");
		user.setFirstName("First Name");
		user.setLastName("Last Name");
		//user.setJoinedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		user.setJoinedDate(new Date());
		user.setLocation("Location");
		users.add(user);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		System.out.println("username=" + username);
		System.out.println("password=" + password);

		Optional<User> userOptional = users.stream().filter(u -> {
			return username.equals(u.getUsername()) && password.equals(u.getPassword());
		}).findFirst();
		System.out.println("userOptional=" + userOptional);

		if (!userOptional.isPresent()) {
			throw new BadCredentialsException("Authentication failed for " + username);
		}

		List<Role> roles = roleDataRepository.findByEmployeeId(Long.valueOf(1136));

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
		if (flag) {
			Role empRole = roleDataRepository.findByRoleName(Roles.EMPLOYEE.getName());
			roles.add(empRole);
		}

		userOptional.get().setRoles(roles);
		return new UserToken(userOptional.get());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
