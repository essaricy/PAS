package com.softvision.ipm.pms.appraisal.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatDeserializer;
import com.softvision.ipm.pms.common.adapter.IndiaDateFormatSerializer;

import lombok.Data;

@Data
public class AppraisalPhaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
    private int id;

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

	@Override
	public String toString() {
		return "\n\tAppraisalPhase [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}

}
