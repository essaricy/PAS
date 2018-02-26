package com.softvision.ipm.pms.goal.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class GoalParamDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_cap_id_seq",sequenceName="goal_cap_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_cap_id_seq")
	private Long id;

	@NotNull(message="Provide a name for parameter")
	@NotBlank(message="Provide a name for parameter. It cannot be empty")
	private String name;

	@NotNull(message="Provide applicable flag for parameter")
	@Pattern(regexp = "Y|N", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String applicable;

	@Override
	public String toString() {
		return "GoalParamDto [id=" + id + ", name=" + name + ", applicable=" + applicable + "]";
	}
	
}
