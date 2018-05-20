package com.softvision.ipm.pms.report.model;

import com.softvision.ipm.pms.report.Constant.PhaseAssignmentReportStatus;

import lombok.Data;

@Data
public class EmployeeAssignmentCount {

	private PhaseAssignmentReportStatus phaseAssignmentReportStatus;

	private int count;
}
