package com.softvision.digital.pms.assign.constant;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CycleAssignmentStatus {

	NOT_INITIATED(0, "Not Initiated", "Appraisal Form has not been initiated"),

	ABRIDGED(100, "Abridged", "Phases summarized and cycle assessment available"),

	CONCLUDED(500, "Concluded", "Assessment is concluded");

	@Getter
	private int code;

	@Getter
	private String name;

	@Getter
	private String description;

	public static CycleAssignmentStatus get(int code) {
		return Arrays.stream(values()).filter(e -> e.getCode() == code).findAny().orElse(null);
	}

}
