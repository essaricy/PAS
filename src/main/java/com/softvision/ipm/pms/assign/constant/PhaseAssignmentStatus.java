package com.softvision.ipm.pms.assign.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PhaseAssignmentStatus {

	NOT_INITIATED(0, "Not Initiated", "Appraisal Form has not been initiated"),

	SELF_APPRAISAL_PENDING(100, "Self-Appraisal Pending", "Initiated and self-appraisal is pending"),

	SELF_APPRAISAL_SAVED(200, "Self-Appraisal drafted", "Self-Appraisal is saved but not submitted"),

	MANAGER_REVIEW_PENDING(300, "Review Pending", "Self-Appraisal is completed and manager review is pending"),

	MANAGER_REVIEW_SAVED(400, "Review Saved", "Manager review is saved but not concluded"),

	CONCLUDED(500, "Concluded", "Assessment is concluded");

	private int code;

	private String name;

	private String title;

	private String description;

	private PhaseAssignmentStatus(int code, String title, String desciption) {
		this.code=code;
		this.name=this.toString();
		this.title=title;
		this.description=desciption;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
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

	public static PhaseAssignmentStatus getNext(int currentCode) {
		PhaseAssignmentStatus next=null;
		for (PhaseAssignmentStatus status : values()) {
			if (status.getCode() > currentCode) {
				//return status;
				if (next == null || status.getCode() < next.getCode()) {
					next=status;
					continue;
				}
			}
		}
		return next;
	}

	public static PhaseAssignmentStatus getNext(PhaseAssignmentStatus status) {
		return getNext(status.getCode());
	}
}
