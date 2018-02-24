package com.softvision.ipm.pms.goal.entity;

import java.io.Serializable;

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
@Entity(name="goal_catp")
public class GoalCatp implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_catp_id_seq",sequenceName="goal_catp_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_catp_id_seq")
	@JsonFormat
	private Long id;

	@NotNull(message="Provide goal template Id for goal template parameter")
	private Long goalCatId;

	@NotNull(message="Provide goal template parameter Id for goal template parameter")
	private Long goalCapId;

	@NotEmpty(message="Provide weightage for goal template parameter")
	@NotBlank(message="Provideweightage for goal template parameter")
	@JsonFormat
	private double weightage;

}
