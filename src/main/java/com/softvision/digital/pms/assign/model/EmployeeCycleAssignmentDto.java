package com.softvision.digital.pms.assign.model;

import java.io.Serializable;
import java.util.List;

import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;

import lombok.Data;

@Data
public class EmployeeCycleAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalCycleDto cycle;

	private EmployeeAssignmentDto employeeAssignment;

	private List<PhaseAssignmentDto> phaseAssignments;

}
