package com.softvision.ipm.pms.assign.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.template.model.TemplateDto;

import lombok.Data;

@Data
public class AssignmentAuditDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private AppraisalPhaseDto phase;

	private TemplateDto template;

	private EmployeeDto assignedTo;

	private EmployeeDto assignedBy;

	private int status;

	private double score;

	private String action;

	private Timestamp timestamp;

}
