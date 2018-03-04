package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.util.Date;

import com.softvision.ipm.pms.employee.entity.Employee;

import lombok.Data;

@Data
public class EmployeeAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long assignmentId;

	private Employee assignedTo;

	private Employee assignedBy;

	private Date assignedAt;

	private int status;

	@Override
	public String toString() {
		return "EmployeeAssignmentDto [assignmentId=" + assignmentId + ", assignedTo=" + assignedTo + ", assignedBy="
				+ assignedBy + ", assignedAt=" + assignedAt + ", status=" + status + "]";
	}

}
