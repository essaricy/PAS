package com.softvision.ipm.pms.template.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.softvision.ipm.pms.goal.entity.GoalParam;

import lombok.Data;

@Data
@Entity
public class TemplateDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="template_detail_id_seq",sequenceName="template_detail_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="template_detail_id_seq")
	private Long id;

	private String apply;

	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "param_id", nullable = false, updatable=false, insertable=false)
	private GoalParam goalParam;

	@Override
	public String toString() {
		return "\n\tTemplateDetail [id=" + id + ", apply=" + apply + ", goalParam=" + goalParam + "]";
	}

}
