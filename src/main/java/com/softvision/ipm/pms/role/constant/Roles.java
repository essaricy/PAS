package com.softvision.ipm.pms.role.constant;

public enum Roles {
	ADMIN("Admin"), MANAGER("Manager"), EMPLOYEE("Employee");

	private String name;

	public String getName() {
		return name;
	}
	private Roles(String name) {
		this.name=name;
	}
	public static Roles get(String role) {
		for (Roles roleName : values()) {
			if (roleName.toString().equalsIgnoreCase(role))
				return roleName;
		}
		return null;
	}
}
