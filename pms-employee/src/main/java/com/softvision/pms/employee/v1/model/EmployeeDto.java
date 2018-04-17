package com.softvision.pms.employee.v1.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmployeeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(notes = "Employee ID")
	@Min(value=1, message="Invalid employeeId")
	private int employeeId;

	@ApiModelProperty(notes = "First Name of the Employee")
	@NotBlank(message="First name is required")
	private String firstName;

	@ApiModelProperty(notes = "Last Name of the Employee")
	@NotBlank(message="Last name is required")
	private String lastName;

	@ApiModelProperty(notes = "Full Name of the Employee")
	private String fullName;

	@ApiModelProperty(notes = "Employment Type")
	@NotBlank(message="Employment type is required")
	private String employmentType;

	@ApiModelProperty(notes = "Designation of the Employee")
	private String designation;

	@ApiModelProperty(notes = "Band of the Employee")
	@NotBlank(message="Band is required")
	private String band;

	@ApiModelProperty(notes = "Joined Date of the Employee")
	@JsonFormat(pattern="dd/MM/YYYY")
	@NotNull(message="Hired on is required")
	private Date hiredOn;

	@ApiModelProperty(notes = "Location of the Employee where he is currently working now")
	private String location;

	@Override
	public String toString() {
		return "EmployeeDto [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", fullName=" + fullName + ", employmentType=" + employmentType + ", designation=" + designation
				+ ", band=" + band + ", hiredOn=" + hiredOn + ", location=" + location + "]";
	}

}
