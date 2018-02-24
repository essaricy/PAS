package com.softvision.ipm.pms.goal.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity(name="goal_cat")
public class GoalCat implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_cat_id_seq",sequenceName="goal_cat_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_cat_id_seq")
	@JsonFormat
	private Long id;

	@NotEmpty(message="Provide a name for goal template")
	@NotBlank(message="Provide a name for goal template")
	@JsonFormat
	private String name;

}
