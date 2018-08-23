package com.softvision.digital.pms.report.constant;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;

import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PhaseAssignmentReportStatus {

	NOT_ASSIGNGED(-1, "Not Assigned", "Template has not been assigned"),
	
	NOT_INITIATED(PhaseAssignmentStatus.NOT_INITIATED),

	SELF_APPRAISAL_PENDING(PhaseAssignmentStatus.SELF_APPRAISAL_PENDING),

	SELF_APPRAISAL_SAVED(PhaseAssignmentStatus.SELF_APPRAISAL_SAVED),

	SELF_APPRAISAL_REVERTED(PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED),

	MANAGER_REVIEW_PENDING(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING),

	MANAGER_REVIEW_SAVED(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED),

	MANAGER_REVIEW_SUBMITTED(PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED),

	EMPLOYEE_AGREED(PhaseAssignmentStatus.EMPLOYEE_AGREED),

	EMPLOYEE_ESCALATED(PhaseAssignmentStatus.EMPLOYEE_ESCALATED),

	CONCLUDED(PhaseAssignmentStatus.CONCLUDED);

	@Getter
	private int code;

	@Getter
	private String name;

	@Getter
	private String description;

	private PhaseAssignmentReportStatus(PhaseAssignmentStatus phaseAssignmentStatus) {
		this(phaseAssignmentStatus.getCode(), phaseAssignmentStatus.getName(), phaseAssignmentStatus.getDescription());
	}

	private PhaseAssignmentReportStatus(int code, String name, String description) {
		this.code=code;
		this.name=name;
		this.description=description;
	}

	public static PhaseAssignmentReportStatus get(int code) {
		return Arrays.stream(values()).filter(e -> e.getCode() == code).findAny().orElse(null);
	}

	public static PhaseAssignmentReportStatus getNext(PhaseAssignmentReportStatus status) {
		return getNext(status.getCode());
	}

	public static PhaseAssignmentReportStatus getNext(int currentCode) {
		PhaseAssignmentReportStatus next=null;
		for (PhaseAssignmentReportStatus status : values()) {
			if (status.getCode() > currentCode && (next == null || status.getCode() < next.getCode())) {
				next=status;
			}
		}
		return next;
	}

	public static PhaseAssignmentReportStatus getPrevious(PhaseAssignmentReportStatus status) {
		return getPrevious(status.getCode());
	}

	public static PhaseAssignmentReportStatus getPrevious(int currentCode) {
		PhaseAssignmentReportStatus previous=null;
		for (PhaseAssignmentReportStatus status : values()) {
			if (status.getCode() < currentCode && (previous == null || status.getCode() > previous.getCode())) {
				previous=status;
			}
		}
		return previous;
	}

}
