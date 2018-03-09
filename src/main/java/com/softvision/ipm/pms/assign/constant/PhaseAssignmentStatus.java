package com.softvision.ipm.pms.assign.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PhaseAssignmentStatus {

	ASSIGNED(0, "Assigned", "Created but not effective"),

	SELF_APPRAISAL_PENDING(10, "Self-appraisal pending", "Initiated and self-appraisal is pending"),

	SELF_APPRAISAL_SAVED(20, "Self-appraisal drafted", "self-appraisal is drafted"),

	SELF_APPRAISAL_COMPLETED(30, "Awaiting Review", "Self-appraisal completed and manager review is pending"),

	MANAGER_REVIEW_COMPLETED(40, "Reviewed", "Manager review is completed"),

	FROZEN(50, "Frozen", "Appraisal phase is frozen");

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

	public static void main(String[] args) {
		System.out.println(getNext(15));
	}

	public static PhaseAssignmentStatus getNext(PhaseAssignmentStatus status) {
		return getNext(status.getCode());
	}
}
