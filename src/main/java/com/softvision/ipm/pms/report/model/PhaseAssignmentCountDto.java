package com.softvision.ipm.pms.report.model;

import java.util.List;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;

import lombok.Data;

@Data
public class PhaseAssignmentCountDto {

	private AppraisalPhase appraisalPhase;

	private List<EmployeeAssignmentCount> employeeAssignmentCounts;
}
