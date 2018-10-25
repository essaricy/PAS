package com.softvision.digital.pms.employee.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement(localName="Employee")
public class SVEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

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
	private Date hiredOn;

	@JsonProperty("Location")
	private String location;

	@JsonProperty("LoginId")
	private String loginId;

	@JsonProperty("Departement")
	private String department;

    @JsonProperty("Division")
    private String division;

    @JsonProperty("Org")
    private String org;

	@JsonInclude
	@JsonProperty("Projects")
	@JacksonXmlProperty(localName="Projects")
	private List<SVEmployeeProject> employeeProjects;

}
