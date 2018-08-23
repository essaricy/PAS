package com.softvision.ipm.pms.assign.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name="audit_phase_assign")
public class AssignmentAudit {

	@Id
	private Long id;

	private Integer phaseId;

	private Integer templateId;

	private Integer employeeId;

	private Integer assignedBy;

	private int status;

	private double score;

	private String action;

	@Column(name="created_ts")
	private Timestamp timestamp;

}
