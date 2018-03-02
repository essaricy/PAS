package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.util.Date;

import com.softvision.ipm.pms.assign.constant.AssignmentPhaseStatus;

import lombok.Data;

@Data
public class EmployeeAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long assignmentId;

	private int phaseId;

	private String phaseName;

	private int assignedToId;

	private String assignedToName;

	private int assignedById;

	private String assignedByName;

	private Date assignedAt;

	private AssignmentPhaseStatus status;

	@Override
	public String toString() {
		return "EmployeeAssignmentDto [assignmentId=" + assignmentId + ", phaseId=" + phaseId + ", phaseName="
				+ phaseName + ", assignedToId=" + assignedToId + ", assignedToName=" + assignedToName
				+ ", assignedById=" + assignedById + ", assignedByName=" + assignedByName + ", assignedAt=" + assignedAt
				+ ", status=" + status + "]";
	}

}
