package com.softvision.ipm.pms.assign.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CycleAssignmentStatus {

	NOT_INITIATED(0, "Not Initiated", "Appraisal Form has not been initiated"),

	ABRIDGED(10, "Abridged", "Phases summarized and cycle assessment available"),

	MANAGER_REVIEW_PENDING(20, "Review Pending", "Self-Appraisal is completed and manager review is pending"),

	MANAGER_REVIEW_SAVED(30, "Review Saved", "Manager review is saved but not concluded"),

	CONCLUDED(40, "Concluded", "Assessment is concluded");

	private int code;

	private String name;

	private String title;

	private String description;

	private CycleAssignmentStatus(int code, String title, String desciption) {
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

	public static CycleAssignmentStatus get(int code) {
		for (CycleAssignmentStatus status : values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}

}
