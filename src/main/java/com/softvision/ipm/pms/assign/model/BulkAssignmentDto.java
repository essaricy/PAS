package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
public class BulkAssignmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long templateId;

	@Min(0)
	private int cycleId;

	@Min(0)
	private int phaseId;

	@Min(0)
	private int assignedBy;

	@NotEmpty(message="At least one employeeId must be provided")
	@NotContainNull(message="Employee Ids list cannot contain null")
	private List<Integer> employeeIds;

	@Override
	public String toString() {
		return "BulkAssignmentDto [templateId=" + templateId + ", cycleId=" + cycleId + ", phaseId=" + phaseId
				+ ", assignedBy=" + assignedBy + ", employeeIds=" + employeeIds + "]";
	}

}
