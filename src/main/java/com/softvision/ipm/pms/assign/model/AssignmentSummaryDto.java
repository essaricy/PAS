package com.softvision.ipm.pms.assign.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class AssignmentSummaryDto extends EmployeeAssignmentDto {

	private static final long serialVersionUID = 1L;

	private double managerScore;

}
