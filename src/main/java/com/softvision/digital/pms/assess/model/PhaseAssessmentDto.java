package com.softvision.digital.pms.assess.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.softvision.digital.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.common.validator.NotContainNull;
import com.softvision.digital.pms.template.model.TemplateHeaderDto;

import lombok.Data;

@Data
@Validated
public class PhaseAssessmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalPhaseDto phase;

	@NotNull(message="Assignment information must be provided")
	private EmployeeAssignmentDto employeeAssignment;

	private List<TemplateHeaderDto> templateHeaders;

	@NotEmpty(message="At least one assessment must be provided")
	@NotContainNull(message="Assess list cannot contain null")
	@Valid
	private List<AssessHeaderDto> assessHeaders;

}
