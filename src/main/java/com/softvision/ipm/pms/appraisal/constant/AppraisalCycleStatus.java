package com.softvision.ipm.pms.appraisal.constant;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AppraisalCycleStatus {

	DRAFT(10),
	READY(20),
	ACTIVE(30),
	COMPLETE(40);

	@Getter
	private int code;

    public static AppraisalCycleStatus get(String status) {
		return Arrays.stream(values()).filter(e -> e.toString().equals(status)).findAny().orElse(null);
	}

}
