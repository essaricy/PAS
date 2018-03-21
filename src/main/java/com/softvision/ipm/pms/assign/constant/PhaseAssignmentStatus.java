package com.softvision.ipm.pms.assign.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PhaseAssignmentStatus {

	NOT_INITIATED(0, "Not Initiated", "Appraisal Form has not been initiated"),

	SELF_APPRAISAL_PENDING(50, "Self-Appraisal Pending", "Initiated and self-appraisal is pending"),

	SELF_APPRAISAL_SAVED(100, "Self-Appraisal in Progess", "Self-Appraisal is saved but not submitted"),

	MANAGER_REVIEW_PENDING(150, "Review Pending", "Self-Appraisal is completed and manager review is pending"),

	MANAGER_REVIEW_SAVED(200, "Review in Progress", "Manager review is saved but not concluded"),

	MANAGER_REVIEW_SUBMITTED(250, "Reviewed", "Manager review is completed and awaiting employee agreement"),

	EMPLOYEE_AGREED(300, "Agreed with Review", "Employee Agreed to the review"),

	EMPLOYEE_ESCALATED(350, "Escalated", "Employee disagreed with the review and escalated"),

	CONCLUDED(500, "Concluded", "Assessment is concluded");

	private int code;

	private String name;

	private String description;

	private PhaseAssignmentStatus(int code, String name, String desciption) {
		this.code=code;
		this.name=name;
		this.description=desciption;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public static PhaseAssignmentStatus get(int code) {
		for (PhaseAssignmentStatus status : values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}

	public static PhaseAssignmentStatus getNext(PhaseAssignmentStatus status) {
		return getNext(status.getCode());
	}

	public static PhaseAssignmentStatus getNext(int currentCode) {
		PhaseAssignmentStatus next=null;
		for (PhaseAssignmentStatus status : values()) {
			if (status.getCode() > currentCode) {
				if (next == null || status.getCode() < next.getCode()) {
					next=status;
					continue;
				}
			}
		}
		return next;
	}

	public static PhaseAssignmentStatus getPrevious(PhaseAssignmentStatus status) {
		return getPrevious(status.getCode());
	}

	public static PhaseAssignmentStatus getPrevious(int currentCode) {
		PhaseAssignmentStatus previous=null;
		for (PhaseAssignmentStatus status : values()) {
			if (status.getCode() < currentCode) {
				if (previous == null || status.getCode() > previous.getCode()) {
					previous=status;
					continue;
				}
			}
		}
		return previous;
	}

	public static void main(String[] args) {
		System.out.println(getPrevious(PhaseAssignmentStatus.EMPLOYEE_AGREED));
	}
}
