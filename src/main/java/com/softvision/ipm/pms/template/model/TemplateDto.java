package com.softvision.ipm.pms.template.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
public class TemplateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotEmpty(message="Provide a name for template")
	@NotBlank(message="Provide a name for template")
	private String name;

	@NotEmpty(message="Provide a updated by for template")
	@NotBlank(message="Provide a updated by for template")
	private String updatedBy;

	@NotEmpty(message="Provide a updated at for template")
	@NotBlank(message="Provide a updated at for template")
	private Date updatedAt;

	@NotNull(message="Headers are required. At least one header should be provided")
	@NotEmpty(message="At least one header must be provided")
	@NotContainNull(message="Headers list cannot contain null")
	private List<TemplateHeaderDto> headers;

	@Override
	public String toString() {
		return "TemplateDto [id=" + id + ", name=" + name + ", updatedBy=" + updatedBy + ", updatedAt=" + updatedAt
				+ ", headers=" + headers + "]";
	}

}
