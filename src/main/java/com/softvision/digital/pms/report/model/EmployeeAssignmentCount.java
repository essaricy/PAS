package com.softvision.digital.pms.report.model;

import com.softvision.digital.pms.report.constant.PhaseAssignmentReportStatus;

import lombok.Data;

@Data
public class EmployeeAssignmentCount {

	private PhaseAssignmentReportStatus phaseAssignmentReportStatus;

	private int count;
}
