package com.softvision.ipm.pms.goal.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity(name="goal_cap")
public class GoalCap implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_cap_id_seq",sequenceName="goal_cap_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_cap_id_seq")
	@JsonFormat
	private Long id;

	@NotNull(message="Provide a name for parameter")
	@NotBlank(message="Provide a name for parameter. It cannot be empty")
	@JsonFormat
	private String name;

	@NotNull(message="Provide apply flag for parameter")
	@Pattern(regexp = "y|n", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String apply;

	@Override
	public String toString() {
		return "GoalCap [id=" + id + ", name=" + name + ", apply=" + apply + "]";
	}
	
}
