package com.softvision.ipm.pms.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softvision.ipm.pms.user.model.User;

public class RestUtil {

	public static int getLoggedInEmployeeId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getDetails();
		int employeeId = user.getEmployeeId();
		return employeeId;
	}

	public static String getLoggedInLoginId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getPrincipal().toString();
	}

}
