package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.softvision.ipm.pms.employee.model.EmployeeDto;

import lombok.Data;

@Data
public class EmployeeAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long assignmentId;

	@NotNull(message="Assigned to Employee information must be provided")
	private EmployeeDto assignedTo;

	@NotNull(message="Assigned by Employee information must be provided")
	private EmployeeDto assignedBy;

	private Date assignedAt;

	private int status;

	private double score;

	@Override
	public String toString() {
		return "EmployeeAssignmentDto [assignmentId=" + assignmentId + ", assignedTo=" + assignedTo + ", assignedBy="
				+ assignedBy + ", assignedAt=" + assignedAt + ", status=" + status + "]";
	}

}
