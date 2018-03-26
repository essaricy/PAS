package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
@Validated
public class AssessHeaderDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@Min(0)
	private long assignId;

	@Min(0)
	private int status;

	@NotNull(message="Assessment date cannot be empty")
	private Date assessDate;

	@Min(value=1, message="Invalid Employee Id")
	private int assessedBy;

	@NotEmpty(message="At least one assess detail must be provided")
	@NotContainNull(message="Assess detail list cannot contain null")
	@Valid
	private List<AssessDetailDto> assessDetails;

	@Override
	public String toString() {
		return "AssessHeaderDto [id=" + id + ", assignId=" + assignId + ", status=" + status + ", assessDate="
				+ assessDate + ", assessedBy=" + assessedBy + ", assessDetails=" + assessDetails + "]";
	}

}
