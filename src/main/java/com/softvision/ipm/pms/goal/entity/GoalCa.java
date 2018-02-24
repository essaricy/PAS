package com.softvision.ipm.pms.goal.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
@Entity(name="goal_ca")
public class GoalCa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_ca_id_seq",sequenceName="goal_ca_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_ca_id_seq")
	@JsonFormat
	private Long id;

	@NotEmpty(message="Provide a name for goal assessment")
	@NotBlank(message="Provide a name for goal assessment")
	@JsonFormat
	@Column(unique=true)
	private String name;

	@NotNull(message="Parameter are required. At least one parameter should be provided")
	@NotEmpty(message="At least one parameter must be provided")
	@NotContainNull(message="Parameters list cannot contain null")
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "ca_id", nullable = false)
	private List<GoalCap> goalCaps;

	@Override
	public String toString() {
		return "GoalCa [id=" + id + ", name=" + name + ", goalCaps=" + goalCaps + "]";
	}

}
