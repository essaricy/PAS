package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;
import java.util.List;

import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

import lombok.Data;

@Data
public class PhaseAssessmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalPhaseDto phase;

	private EmployeeAssignmentDto employeeAssignment;

	private List<TemplateHeaderDto> templateHeaders;

	private List<AssignmentAssessmentsDto> assignmentAssessments;

	@Override
	public String toString() {
		return "PhaseAssessmentDto [phase=" + phase + ", templateHeaders=" + templateHeaders
				+ ", assignmentAssessments=" + assignmentAssessments + "]";
	}

}
