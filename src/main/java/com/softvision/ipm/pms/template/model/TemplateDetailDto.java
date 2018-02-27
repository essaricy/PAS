package com.softvision.ipm.pms.template.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class TemplateDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@Min(value=1, message="Param Id cannot be zero")
	private long paramId;

	@NotBlank(message="Provide a name for template detail")
	private String paramName;

	@NotBlank(message="Provide applicable flag for parameter")
	@Pattern(regexp = "Y|N", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String apply;

	@Override
	public String toString() {
		return "TemplateDetailDto [id=" + id + ", paramId=" + paramId + ", paramName=" + paramName + ", apply=" + apply
				+ "]";
	}

}
