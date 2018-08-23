package com.softvision.digital.pms.assess.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

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

	@Min(0)
	private int stage;

	@NotNull(message="Assessment date cannot be empty")
	private Date assessDate;

	@Min(value=1, message="Invalid Employee Id")
	private int assessedBy;

	@NotEmpty(message="At least one assess detail must be provided")
	@Valid
	private List<@NotNull(message="Assess detail list cannot contain null") AssessDetailDto> assessDetails;

}