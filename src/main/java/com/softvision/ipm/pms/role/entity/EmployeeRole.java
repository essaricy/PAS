package com.softvision.ipm.pms.role.entity;

import lombok.Data;

@Data
public class EmployeeRole {
	private int id;
	private String managerFlag;
	@Override
	public String toString() {
		return "EmployeeRole [id=" + id + ", managerFlag=" + managerFlag + "]";
	}
}
