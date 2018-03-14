package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.util.List;

import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;

import lombok.Data;

@Data
public class EmployeeCycleAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalCycleDto cycle;

	EmployeeAssignmentDto employeeAssignment;

	List<PhaseAssignmentDto> phaseAssignments;

	@Override
	public String toString() {
		return "ManagerCycleAssignmentDto [cycle=" + cycle + ", employeeAssignment=" + employeeAssignment
				+ ", phaseAssignments=" + phaseAssignments + "]";
	}

}
