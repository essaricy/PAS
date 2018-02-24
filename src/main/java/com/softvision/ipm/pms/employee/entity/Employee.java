package com.softvision.ipm.pms.employee.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
@Entity
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty("EmployeeId")
	private int employeeId;

	@JsonProperty("FirstName")
	private String firstName;

	@JsonProperty("LastName")
	private String lastName;

	@JsonProperty("EmploymentType")
	private String employmentType;

	@JsonProperty("Designation")
	private String designation;

	@JsonProperty("Band")
	private String band;

	@JsonProperty("HiredOn")
	//@JsonFormat(pattern="dd/MM/YYYY")
	private Date hiredOn;

	@JsonProperty("Location")
	private String location;

	@JsonProperty("LoginId")
	private String loginId;

	@Transient
	@JsonInclude
	@JsonProperty("Projects")
	@JacksonXmlProperty(localName="Projects")
	private List<EmployeeProject> employeeProjects;

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", employmentType=" + employmentType + ", designation=" + designation + ", band=" + band
				+ ", hiredOn=" + hiredOn + ", location=" + location + ", loginId=" + loginId + ", employeeProjects="
				+ employeeProjects + "]";
	}

}
