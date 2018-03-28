package com.softvision.ipm.pms.role.constant;

public enum Roles {
	ADMIN(1, "Admin"), MANAGER(2, "Manager"), EMPLOYEE(3, "Employee");

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
		for (Roles r : Roles.values()) {
			if (r.getName().equalsIgnoreCase(role))
				return r;
		}
		return null;
	}

	public static Roles get(int code) {
		for (Roles r : Roles.values()) {
			if (r.getCode() == code)
				return r;
		}
		return null;
	}

}
