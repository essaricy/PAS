package com.softvision.ipm.pms.assign.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AssignmentPhaseStatus {

	ASSIGNED(0, "Assigned", "Created but not effective"),

	SELF_APPRAISAL_PENDING(10, "Self-appraisal pending", "Initiated and self-appraisal is pending"),

	SELF_APPRAISAL_COMPLETED(20, "Awaiting Review", "Self-appraisal completed and manager review is pending"),

	MANAGER_REVIEW_COMPLETED(30, "Reviewed", "Manager review is completed"),

	FROZEN(40, "Frozen", "Appraisal phase is frozen");

	private int code;

	private String name;

	private String title;

	private String description;

	private AssignmentPhaseStatus(int code, String title, String desciption) {
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

	public static AssignmentPhaseStatus get(int code) {
		for (AssignmentPhaseStatus status : values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}

}
