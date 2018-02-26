package com.softvision.ipm.pms.template.entity;

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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softvision.ipm.pms.common.validator.NotContainNull;
import com.softvision.ipm.pms.goal.entity.Goal;

import lombok.Data;

@Data
@Entity
public class TemplateHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="template_header_id_seq",sequenceName="template_header_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="template_header_id_seq")
	@JsonFormat
	private Long id;

	@JsonFormat
	private int weightage;

	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.DETACH, orphanRemoval=true)
	@JoinColumn(name = "ca_id", nullable = false)
	private Goal goalCa;

	@NotNull(message="Details are required. At least one detail should be provided")
	@NotEmpty(message="At least one detail must be provided")
	@NotContainNull(message="Details list cannot contain null")
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "header_id", nullable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TemplateDetail> templateDetails;

	@Override
	public String toString() {
		return "TemplateHeader [id=" + id + ", weightage=" + weightage + ", goalCa=" + goalCa + ", templateDetails="
				+ templateDetails + "]";
	}

}
