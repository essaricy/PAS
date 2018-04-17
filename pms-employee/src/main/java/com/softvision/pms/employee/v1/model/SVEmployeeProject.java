package com.softvision.pms.employee.v1.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement(localName="EmployeeProjects")
public class SVEmployeeProject implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("ManagerId")
	private int managerId;

	@JsonProperty("ProjectId")
	private int projectId;

	@JsonProperty("ProjectName")
	private String projectName;

	@JsonProperty("ProjectStartDate")
	private Date projectStartDate;

	@JsonProperty("ProjectEndDate")
	private Date projectEndDate;

	@JsonProperty("AllocatedFrom")
	private Date allocatedFrom;

	@JsonProperty("AllocatedTo")
	private Date allocatedTo;

	@JsonProperty("AllocationStatus")
	private String allocationStatus;

	@JsonProperty("Billing")
	private String billing;

	@Override
	public String toString() {
		return "\nEmployeeProject [managerId=" + managerId + ", projectId=" + projectId + ", projectName=" + projectName
				+ ", projectStartDate=" + projectStartDate + ", projectEndDate=" + projectEndDate + ", allocatedFrom="
				+ allocatedFrom + ", allocatedTo=" + allocatedTo + ", allocationStatus=" + allocationStatus
				+ ", billing=" + billing + "]";
	}

}
