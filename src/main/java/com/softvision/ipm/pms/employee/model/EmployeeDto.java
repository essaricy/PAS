package com.softvision.ipm.pms.employee.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EmployeeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(value=1, message="Invalid employeeId")
	private int employeeId;

	@NotBlank(message="First name is required")
	private String firstName;

	@NotBlank(message="Last name is required")
	private String lastName;

	private String fullName;

	@NotBlank(message="Employment type is required")
	private String employmentType;

	private String designation;

	@NotBlank(message="Band is required")
	private String band;

	@JsonFormat(pattern="dd/MM/YYYY")
	@NotNull(message="Hired on is required")
	private Date hiredOn;

	private String location;

	@NotBlank(message="Login Id is required")
	private String loginId;

	private String department;

    private String division;

    private String org;

}
