package com.softvision.digital.pms.template.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softvision.digital.pms.common.adapter.IndiaDateFormatDeserializer;
import com.softvision.digital.pms.common.adapter.IndiaDateFormatSerializer;
import com.softvision.digital.pms.employee.model.EmployeeDto;

import lombok.Data;

@Data
@Validated
public class TemplateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@NotBlank(message="Provide a name for template")
	private String name;

	@NotNull(message="Provide a updated by for template")
	private EmployeeDto updatedBy;

	@NotNull(message="Provide a updated at for template")
	@JsonSerialize(using=IndiaDateFormatSerializer.class)
	@JsonDeserialize(using=IndiaDateFormatDeserializer.class)
	private Date updatedAt;

	@NotEmpty(message="At least one header must be provided")
	private List<@NotNull(message="Headers list cannot contain null") TemplateHeaderDto> headers;

}
