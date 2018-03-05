package com.softvision.ipm.pms.assign.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity(name="emp_phase_assign")
public class EmployeePhaseAssignment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="emp_phase_assign_id_seq",sequenceName="emp_phase_assign_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="emp_phase_assign_id_seq")
	private Long id;

	private int phaseId;

	private long templateId;

	private int employeeId;

	private int assignedBy;

	private Date assignedAt;

	private int status;

	@Override
	public String toString() {
		return "EmployeePhaseAssignment [id=" + id + ", phaseId=" + phaseId + ", templateId=" + templateId
				+ ", employeeId=" + employeeId + ", assignedBy=" + assignedBy + ", assignedAt=" + assignedAt
				+ ", status=" + status + "]";
	}

}
