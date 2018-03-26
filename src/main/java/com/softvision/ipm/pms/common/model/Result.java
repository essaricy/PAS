package com.softvision.ipm.pms.common.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SUCCESS="SUCCESS";

	public static final String FAILURE="FAILURE";

	private String code;

	private String message;

	private Object content;

	@Override
	public String toString() {
		return "Result [code=" + code + ", message=" + message + ", content=" + content + "]";
	}

}
