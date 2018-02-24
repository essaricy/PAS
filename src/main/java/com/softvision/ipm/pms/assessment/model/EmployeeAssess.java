package com.softvision.ipm.pms.assessment.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity(name="emp_asses")
public class EmployeeAssess implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="emp_assess_id_seq",sequenceName="emp_assess_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="emp_assess_id_seq")
	@JsonFormat
	private Long id;

	@NotNull(message="Provide Employee assignment Id for assessment")
	@JsonFormat
	private Long empAssignId;

	@NotNull(message="Provide assessment date for assessment")
	@JsonFormat
	private Date assessDate;

	@NotNull(message="Provide assessed by employee number for assessment")
	@JsonFormat
	private String assessedBy;

	@NotEmpty(message="Provide score for assessment")
	@NotBlank(message="Provide score for assessment")
	@JsonFormat
	private double score;
	
	@NotNull(message="Provide comments for assignment")
	@NotEmpty(message="Provide comments for assignment")
	@NotBlank(message="Provide comments for assignment")
	@JsonFormat
	private String comments;

}
