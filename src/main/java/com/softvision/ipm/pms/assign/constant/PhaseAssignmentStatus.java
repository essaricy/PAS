package com.softvision.ipm.pms.assign.constant;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PhaseAssignmentStatus {

	NOT_INITIATED(0, "Not Initiated", "Appraisal Form has not been initiated"),

	SELF_APPRAISAL_PENDING(50, "Self-Appraisal Pending", "Initiated and self-appraisal is pending"),

	SELF_APPRAISAL_SAVED(100, "Self-Appraisal in Progess", "Self-Appraisal is saved but not submitted"),

	SELF_APPRAISAL_REVERTED(125, "Self-Appraisal is reverted", "Self-Appraisal is completed but reverted by the manager"),

	MANAGER_REVIEW_PENDING(150, "Review Pending", "Self-Appraisal is completed and manager review is pending"),

	MANAGER_REVIEW_SAVED(200, "Review in Progress", "Manager review is saved but not concluded"),

	MANAGER_REVIEW_SUBMITTED(250, "Reviewed", "Manager review is completed and awaiting employee agreement"),

	EMPLOYEE_AGREED(300, "Agreed with Review", "Employee Agreed to the review"),

	EMPLOYEE_ESCALATED(350, "Escalated", "Employee disagreed with the review and escalated"),

	CONCLUDED(500, "Concluded", "Assessment is concluded");

	@Getter
	private int code;

	@Getter
	private String name;

	@Getter
	private String description;

	public static PhaseAssignmentStatus get(int code) {
		return Arrays.stream(values()).filter(e -> e.getCode() == code).findAny().orElse(null);
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

}
