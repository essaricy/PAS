package com.softvision.ipm.pms.role.constant;

public enum Roles {
	ADMIN(1, "Admin"), MANAGER(2, "Manager"), EMPLOYEE(3, "Employee");

	private int code;

	private String name;
	
	public static final int ADMIN_ROLE_ID = 1;
	public static final int MANAGER_ROLE_ID = 2;

	private Roles(int code, String name) {
		this.code=code;
		this.name=name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Roles get(String role) {
		for (Roles roleName : values()) {
			if (roleName.toString().equalsIgnoreCase(role))
				return roleName;
		}
		return null;
	}

}
