package com.softvision.ipm.pms.appraisal.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
@Entity(name="appr_cycle")
public class AppraisalCycle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="appr_cycle_id_seq",sequenceName="appr_cycle_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="appr_cycle_id_seq")
	@JsonFormat
	private Long id;

	@NotNull(message="Provide a name for cycle")
	@NotBlank(message="Provide a name for cycle. It cannot be empty")
	@JsonFormat
	private String name;

	@NotNull(message="Provide a start date for cycle")
	//@JsonFormat(pattern="dd/MM/YYYY")
	private Date startDate;

	@NotNull(message="Provide an end date for cycle")
	//@JsonFormat(pattern="dd/MM/YYYY")
	private Date endDate;

	@NotNull(message="Provide a eligibility cutoff date for cycle")
	//@JsonFormat(pattern="dd/MM/YYYY")
	private Date cutoffDate;

	@NotEmpty(message="Provide a status for cycle")
	@NotBlank(message="Provide a status for cycle")
	@Pattern(regexp = "DRAFT|ACTIVE|COMPLETE", flags = Pattern.Flag.CASE_INSENSITIVE)
	@JsonFormat
	private String status;

	@NotNull(message="Phases are required. At least one phase should be provided")
	@NotEmpty(message="At least one phase must be provided")
	@NotContainNull(message="Phases list cannot contain null")
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "cycle_id", nullable = false)
	private List<AppraisalPhase> phases;

	@Override
	public String toString() {
		return "AppraisalCycle [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", cutoffDate=" + cutoffDate + ", status=" + status + ", phases=" + phases + "]";
	}

}
