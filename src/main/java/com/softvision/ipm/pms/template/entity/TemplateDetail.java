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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softvision.ipm.pms.goal.entity.GoalCap;

import lombok.Data;

@Data
@Entity
public class TemplateDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="template_detail_id_seq",sequenceName="template_detail_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="template_detail_id_seq")
	@JsonFormat
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "cap_id", nullable = false)
	private GoalCap goalCap;
}
