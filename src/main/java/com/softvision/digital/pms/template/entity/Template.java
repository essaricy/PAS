package com.softvision.digital.pms.template.entity;

import java.io.Serializable;
import java.util.Date;
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
@Entity
public class Template implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="template_id_seq",sequenceName="template_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="template_id_seq")
	private Long id;

	@Column(unique=true)
	private String name;

	private int updatedBy;

	private Date updatedAt;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "template_id", nullable = false)
	private List<TemplateHeader> templateHeaders;

}
