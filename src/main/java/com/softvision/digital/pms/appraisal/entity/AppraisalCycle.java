package com.softvision.digital.pms.appraisal.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity(name="appr_cycle")
public class AppraisalCycle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="appr_cycle_id_seq",sequenceName="appr_cycle_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="appr_cycle_id_seq")
	private Integer id;

	@Column(unique=true)
	private String name;

	@Column(unique=true)
	private Date startDate;

	@Column(unique=true)
	private Date endDate;

	private Date cutoffDate;

	private String status;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "cycle_id", nullable = false)
	private List<AppraisalPhase> phases;

}
