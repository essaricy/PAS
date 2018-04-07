package com.softvision.ipm.pms.appraisal.constant;

import java.util.Arrays;

public enum AppraisalCycleStatus {
	DRAFT(10),
	READY(20),
	ACTIVE(30),
	COMPLETE(40);

    private int code;

    private AppraisalCycleStatus(int code) {
        this.code=code;
    }

	public int getCode() {
        return code;
    }

    public static AppraisalCycleStatus get(String status) {
		return Arrays.stream(values()).filter(e -> e.toString().equals(status)).findAny().orElse(null);
	}
}
