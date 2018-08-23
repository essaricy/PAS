package com.softvision.digital.pms.report.model;

import java.io.Serializable;

import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;

import lombok.Data;

@Data
public class PhasewiseEmployeeStatusCountDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private PhaseAssignmentStatus phaseAssignmentStatus;

	private int count;

}
