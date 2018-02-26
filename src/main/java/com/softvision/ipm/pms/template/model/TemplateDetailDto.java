package com.softvision.ipm.pms.template.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class TemplateDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private long paramId;

	@NotEmpty(message="Provide a name for template detail")
	@NotBlank(message="Provide a name for template detail")
	private String paramName;

	@NotNull(message="Provide applicable flag for parameter")
	@Pattern(regexp = "Y|N", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String paramApply;

	@Override
	public String toString() {
		return "TemplateDetailDto [id=" + id + ", paramId=" + paramId + ", paramName=" + paramName + ", paramApply="
				+ paramApply + "]";
	}

}
