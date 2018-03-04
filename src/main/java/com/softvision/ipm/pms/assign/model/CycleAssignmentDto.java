package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.util.List;

import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;

import lombok.Data;

@Data
public class CycleAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalCycleDto cycle;

	List<EmployeeAssignmentDto> employeeAssignments;

	List<PhaseAssignmentDto> phaseAssignments;

	@Override
	public String toString() {
		return "CycleAssignmentDto [cycle=" + cycle + ", employeeAssignments=" + employeeAssignments
				+ ", phaseAssignments=" + phaseAssignments + "]";
	}

}
