package com.softvision.digital.pms.goal.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class GoalParamDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@NotBlank(message="Provide a name for parameter. It cannot be empty")
	private String name;

	@NotBlank(message="Provide applicable flag for parameter")
	@Pattern(regexp = "Y|N", flags = Pattern.Flag.CASE_INSENSITIVE, message="Allowed values for applicable are {regexp}")
	private String applicable;

}