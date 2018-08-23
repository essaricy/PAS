package com.softvision.digital.pms.report.model;

import java.util.List;

import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;

import lombok.Data;

@Data
public class PhaseAssignmentCountDto {

	private AppraisalPhase appraisalPhase;

	private List<EmployeeAssignmentCount> employeeAssignmentCounts;
}
