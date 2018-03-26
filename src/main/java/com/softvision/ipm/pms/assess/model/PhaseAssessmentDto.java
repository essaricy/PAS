package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.common.validator.NotContainNull;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

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

	@Override
	public String toString() {
		return "PhaseAssessmentDto [phase=" + phase + ", employeeAssignment=" + employeeAssignment
				+ ", templateHeaders=" + templateHeaders + ", assessHeaders=" + assessHeaders + "]";
	}

}
