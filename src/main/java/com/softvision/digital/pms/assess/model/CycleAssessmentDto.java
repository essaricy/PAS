package com.softvision.digital.pms.assess.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.template.model.TemplateHeaderDto;

import lombok.Data;

@Data
@Validated
public class CycleAssessmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppraisalCycleDto cycle;

	@NotNull(message="Assignment information must be provided")
	private EmployeeAssignmentDto employeeAssignment;

	private List<TemplateHeaderDto> templateHeaders;

}
