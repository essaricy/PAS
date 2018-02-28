package com.softvision.ipm.pms.goal.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity(name="goal_cap")
public class GoalParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="goal_cap_id_seq",sequenceName="goal_cap_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="goal_cap_id_seq")
	private Long id;

	private String name;

	private String apply;

	@Override
	public String toString() {
		return "GoalParam [id=" + id + ", name=" + name + ", apply=" + apply + "]";
	}
	
}
