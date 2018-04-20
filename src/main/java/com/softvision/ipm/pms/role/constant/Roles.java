package com.softvision.ipm.pms.role.constant;

import java.util.Arrays;

public enum Roles {

    ADMIN(1, "Admin"),
	MANAGER(2, "Manager"),
	EMPLOYEE(3, "Employee"),
	SUPPORT(4, "Support");

	private int code;

	private String name;

	private Roles(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Roles get(String role) {
		return Arrays.stream(values()).filter(e -> e.getName().equalsIgnoreCase(role)).findFirst().orElse(null);
	}

	public static Roles get(int code) {
		return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
	}

}
