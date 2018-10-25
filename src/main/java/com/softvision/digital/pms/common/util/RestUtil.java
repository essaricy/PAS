package com.softvision.digital.pms.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softvision.digital.pms.auth.model.User;

public class RestUtil {

	private RestUtil() {}

	public static int getLoggedInEmployeeId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getDetails();
		return user.getEmployeeId();
	}

	public static String getLoggedInLoginId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getPrincipal().toString();
	}

}
