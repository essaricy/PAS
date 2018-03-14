package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.common.validator.NotContainNull;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

import lombok.Data;

@Data
@Validated
public class CycleAssessmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalCycleDto cycle;

	@NotNull(message="Assignment information must be provided")
	private EmployeeAssignmentDto employeeAssignment;

	private List<TemplateHeaderDto> templateHeaders;

	@NotEmpty(message="At least one assessment must be provided")
	@NotContainNull(message="Assess list cannot contain null")
	@Valid
	private List<CycleAssessHeaderDto> cycleAssessmentHeaders;

	@Override
	public String toString() {
		return "CycleAssessmentDto [cycle=" + cycle + ", employeeAssignment=" + employeeAssignment
				+ ", templateHeaders=" + templateHeaders + ", cycleAssessmentHeaders=" + cycleAssessmentHeaders + "]";
	}

}
