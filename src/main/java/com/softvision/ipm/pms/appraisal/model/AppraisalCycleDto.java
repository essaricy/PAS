package com.softvision.ipm.pms.appraisal.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatDeserializer;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatSerializer;
import com.softvision.ipm.pms.common.validator.NotContainNull;
import com.softvision.ipm.pms.constant.AppraisalCycleStatus;

import lombok.Data;

@Data
public class AppraisalCycleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	@NotNull(message="Provide a name for cycle")
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

	@NotNull(message="Phases are required. At least one phase should be provided")
	@NotEmpty(message="At least one phase must be provided")
	@NotContainNull(message="Phases list cannot contain null")
	private List<AppraisalPhaseDto> phases;

	@Override
	public String toString() {
		return "AppraisalCycleDto [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", cutoffDate=" + cutoffDate + ", status=" + status + ", phases=" + phases + "]";
	}

}
