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

	@Min(value=0, message="Invalid Template Id")
	private long templateId;

    @Min(value=0, message="Invalid Cycle Id")
    private int cycleId;

	@Min(value=0, message="Invalid Phase Id")
	private int phaseId;

	@Min(value=1, message="Invalid employee Id for assignedBy")
	private int assignedBy;

	@NotEmpty(message="At least one employeeId must be provided")
	@NotContainNull(message="Employee Ids list cannot contain null")
	private List<Integer> employeeIds;

}
