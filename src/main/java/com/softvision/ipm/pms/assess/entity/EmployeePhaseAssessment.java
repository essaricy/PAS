package com.softvision.ipm.pms.assess.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity(name="emp_phase_assess")
public class EmployeePhaseAssessment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="emp_phase_assess_id_seq",sequenceName="emp_phase_assess_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="emp_phase_assess_id_seq")
	private Long id;

	private Long assignId;

	private Integer assessType;

	private Date assessDate;

	private Integer assessedBy;

	private Long goalId;

	private Double rating;

	private Double score;

	private String comments;

}
