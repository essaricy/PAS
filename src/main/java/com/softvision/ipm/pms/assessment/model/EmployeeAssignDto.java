package com.softvision.ipm.pms.assessment.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatDeserializer;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatSerializer;

import lombok.Data;

@Data
@Validated
public class EmployeeAssignDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(value=0)
	private Long id;

	@Min(value=1, message="Employee number cannot be zero")
	private int employee_number;
	
	private String employee_name;

	@Min(value=1, message="Appraisal cycle id cannot be zero")
	private Long appraisal_cycle_id;
	
	private String appraisalCycleName;

	@Min(value=1, message="Appraisal phase id cannot be zero")
	private Long appraisal_phase_id;
	
	private String appraisalPhaseName;
	
	@Min(value=1, message="Template phase id cannot be zero")
	private Long template_id;
	
	private String appraisalTemplateName;
	

	@NotBlank(message="Enter Assigned by ")
	private String assignedBy;
	
	
	@NotNull(message="Assigned an date")
	@JsonSerialize(using=IndiaDateFormatSerializer.class)
	@JsonDeserialize(using=IndiaDateFormatDeserializer.class)
	private Date assignedAt;


	@Override
	public String toString() {
		return "EmployeeAssignDto [id=" + id + ", employee_number=" + employee_number + ", employee_name="
				+ employee_name + ", appraisal_cycle_id=" + appraisal_cycle_id + ", appraisalCycleName="
				+ appraisalCycleName + ", appraisal_phase_id=" + appraisal_phase_id + ", appraisalPhaseName="
				+ appraisalPhaseName + ", template_id=" + template_id + ", appraisalTemplateName="
				+ appraisalTemplateName + ", assignedBy=" + assignedBy + ", assignedAt=" + assignedAt + "]";
	}
	
}
