package com.softvision.digital.pms.goal.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class GoalDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@NotBlank(message="Provide a name for goal")
	private String name;

	@NotEmpty(message="At least one parameter must be provided")
	private List<@NotNull(message="Parameters list cannot contain null") GoalParamDto> params;

}
