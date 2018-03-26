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
@Entity(name="cycle_assign")
public class CycleAssignment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="cycle_assign_id_seq",sequenceName="cycle_assign_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cycle_assign_id_seq")
	private Long id;

	private int cycleId;

	private long templateId;

	private int employeeId;

	private int assignedBy;

	private Date assignedAt;

	private int status;

	private double score;

	private Integer submittedTo;

	@Override
	public String toString() {
		return "CycleAssignment [id=" + id + ", cycleId=" + cycleId + ", templateId=" + templateId + ", employeeId="
				+ employeeId + ", assignedBy=" + assignedBy + ", assignedAt=" + assignedAt + ", status=" + status
				+ ", score=" + score + ", submittedTo=" + submittedTo + "]";
	}

}
