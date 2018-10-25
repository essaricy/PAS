package com.softvision.digital.pms.assign.model;

import java.io.Serializable;
import java.util.List;

import com.softvision.digital.pms.appraisal.model.AppraisalPhaseDto;

import lombok.Data;

@Data
public class PhaseAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalPhaseDto phase;

	private List<? extends EmployeeAssignmentDto> employeeAssignments;

}
