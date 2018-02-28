package com.softvision.ipm.pms.common.constants;

public enum YesNo {

	YES(true, "Y"),
	NO(false, "N");

	private boolean val;
	private String code;

	private YesNo(boolean val, String code) {
		this.val=val;
		this.code=code;
	}

	public String getCode() {
		return code;
	}

	public boolean isVal() {
		return val;
	}

	public static YesNo get(boolean value) {
		for (YesNo option : values()) {
			if (option.isVal() == value) {
				return option;
			}
		}
		return null;
	}

	public static YesNo get(String value) {
		for (YesNo option : values()) {
			if (option.getCode().equalsIgnoreCase(value) || option.toString().equalsIgnoreCase(value)) {
				return option;
			}
		}
		return null;
	}

	/*public static YesNo getByCode(String code) {
		for (YesNo option : values()) {
			if (option.getCode().equalsIgnoreCase(code)) {
				return option;
			}
		}
		return null;
	}

	public static YesNo getByValue(String value) {
		for (YesNo option : values()) {
			if (option.toString().equalsIgnoreCase(value)) {
				return option;
			}
		}
		return null;
	}*/

}
