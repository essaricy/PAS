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

import lombok.Data;

@Data
@Entity(name="goal_ca")
public class Goal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_ca_id_seq",sequenceName="goal_ca_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_ca_id_seq")
	private Long id;

	@Column(unique=true)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "ca_id", nullable = false)
	private List<GoalParam> params;

	@Override
	public String toString() {
		return "Goal [id=" + id + ", name=" + name + ", params=" + params + "]";
	}

}
