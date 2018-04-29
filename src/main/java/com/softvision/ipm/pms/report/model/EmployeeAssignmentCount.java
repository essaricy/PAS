package com.softvision.ipm.pms.report.model;

import com.softvision.ipm.pms.report.Constant.EmployeeAssignmentStatus;

import lombok.Data;

@Data
public class EmployeeAssignmentCount {
	private EmployeeAssignmentStatus employeeAssignmentStatus;

	private int count;
}
