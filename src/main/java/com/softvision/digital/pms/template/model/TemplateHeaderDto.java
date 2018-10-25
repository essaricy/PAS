package com.softvision.digital.pms.template.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.softvision.digital.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
@Validated
public class TemplateHeaderDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@Min(value=1, message="Goal Id cannot be zero")
	private long goalId;

	@NotBlank(message="Provide a name for template header")
	private String goalName;

	@Min(value=0, message="weightage must be greater than or equal to {value}")
	@Max(value=100, message="weightage must be less than or equal to {value}")
	private int weightage;

	@NotEmpty(message="At least one detail must be provided")
	@NotContainNull(message="Details list cannot contain null")
	@Valid
	private List<TemplateDetailDto> details;

}
