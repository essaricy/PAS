package com.softvision.ipm.pms.appraisal.constant;

public enum AppraisalCycleStatus {
	DRAFT,
	READY,
	ACTIVE,
	COMPLETE;

	public static AppraisalCycleStatus get(String status) {
		for (AppraisalCycleStatus cycleStatus : values()) {
			if (cycleStatus.toString().equals(status)) {
				return cycleStatus;
			}
		}
		return null;
	}
}
