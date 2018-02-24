package com.softvision.ipm.pms.appraisal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity(name="appr_phase")
public class AppraisalPhase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="appr_phase_id_seq",sequenceName="appr_phase_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="appr_phase_id_seq")
    private long id;

	@NotNull(message="Provide a name for cycle")
	@NotBlank(message="Provide a name for cycle. It cannot be empty")
	@JsonFormat
	private String name;

	@NotNull(message="Provide a start date for cycle")
	//@DateTimeFormat(pattern="dd/MM/YYYY")
	private Date startDate;

	@NotNull(message="Provide an end date for cycle")
	//@DateTimeFormat(pattern="dd/MM/YYYY")
	private Date endDate;

	@Override
	public String toString() {
		return "AppraisalPhase [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}

}
