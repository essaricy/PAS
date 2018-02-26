package com.softvision.ipm.pms.goal.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
public class GoalDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	@NotEmpty(message="Provide a name for goal")
	@NotBlank(message="Provide a name for goal")
	private String name;

	@NotNull(message="Parameter are required. At least one parameter should be provided")
	@NotEmpty(message="At least one parameter must be provided")
	@NotContainNull(message="Parameters list cannot contain null")
	private List<GoalParamDto> params;

	@Override
	public String toString() {
		return "GoalDto [id=" + id + ", name=" + name + ", params=" + params + "]";
	}

}
