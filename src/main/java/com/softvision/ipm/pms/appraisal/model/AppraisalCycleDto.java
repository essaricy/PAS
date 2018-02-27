package com.softvision.ipm.pms.appraisal.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatDeserializer;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatSerializer;
import com.softvision.ipm.pms.common.validator.NotContainNull;
import com.softvision.ipm.pms.constant.AppraisalCycleStatus;

import lombok.Data;

@Data
@Validated
public class AppraisalCycleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(value=0)
	private long id;

	@NotBlank(message="Provide a name for cycle. It cannot be empty")
	private String name;

	@NotNull(message="Provide a start date for cycle")
	@JsonSerialize(using=IndiaDateFormatSerializer.class)
	@JsonDeserialize(using=IndiaDateFormatDeserializer.class)
	private Date startDate;

	@NotNull(message="Provide an end date for cycle")
	@JsonSerialize(using=IndiaDateFormatSerializer.class)
	@JsonDeserialize(using=IndiaDateFormatDeserializer.class)
	private Date endDate;

	@NotNull(message="Provide a eligibility cutoff date for cycle")
	@JsonSerialize(using=IndiaDateFormatSerializer.class)
	@JsonDeserialize(using=IndiaDateFormatDeserializer.class)
	private Date cutoffDate;

	@NotNull(message="Provide a status for cycle")
	private AppraisalCycleStatus status;

	@NotEmpty(message="At least one phase must be provided")
	@NotContainNull(message="Phases list cannot contain null")
	@Valid
	private List<AppraisalPhaseDto> phases;

	@Override
	public String toString() {
		return "AppraisalCycleDto [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", cutoffDate=" + cutoffDate + ", status=" + status + ", phases=" + phases + "]";
	}

}
