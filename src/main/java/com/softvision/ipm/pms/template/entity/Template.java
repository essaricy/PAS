package com.softvision.ipm.pms.template.entity;

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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
@Entity
public class Template implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="template_id_seq",sequenceName="template_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="template_id_seq")
	@JsonFormat
	private Long id;

	@NotEmpty(message="Provide a name for template")
	@NotBlank(message="Provide a name for template")
	@JsonFormat
	@Column(unique=true)
	private String name;

	@NotEmpty(message="Provide a updated by for template")
	@NotBlank(message="Provide a updated by for template")
	@JsonFormat
	private String updatedBy;

	@NotEmpty(message="Provide a updated at for template")
	@NotBlank(message="Provide a updated at for template")
	@JsonFormat
	private Date updatedAt;

	@NotNull(message="Headers are required. At least one header should be provided")
	@NotEmpty(message="At least one header must be provided")
	@NotContainNull(message="Headers list cannot contain null")
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "template_id", nullable = false)
	private List<TemplateHeader> templateHeaders;

	@Override
	public String toString() {
		return "Template [id=" + id + ", name=" + name + ", updatedBy=" + updatedBy + ", updatedAt=" + updatedAt
				+ ", templateHeaders=" + templateHeaders + "]";
	}

}
