package com.softvision.ipm.pms.goal.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.softvision.ipm.pms.common.validator.NotContainNull;

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
	@NotContainNull(message="Parameters list cannot contain null")
	@Valid
	private List<GoalParamDto> params;

	@Override
	public String toString() {
		return "GoalDto [id=" + id + ", name=" + name + ", params=" + params + "]";
	}

}
