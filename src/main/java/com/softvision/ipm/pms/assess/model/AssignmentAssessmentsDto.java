package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AssignmentAssessmentsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<EmployeeAssessmentDto> employeeAssessments;

}
