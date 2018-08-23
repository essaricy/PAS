package com.softvision.digital.pms.template.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.softvision.digital.pms.goal.entity.Goal;

import lombok.Data;

@Data
@Entity
public class TemplateHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="template_header_id_seq",sequenceName="template_header_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="template_header_id_seq")
	private Long id;

	private int weightage;

	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.DETACH, orphanRemoval=false)
	@JoinColumn(name = "goal_id", nullable = false, updatable=false, insertable=false)
	private Goal goal;

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "header_id", nullable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TemplateDetail> templateDetails;

}
