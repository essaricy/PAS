package com.softvision.ipm.pms.common.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SUCCESS="SUCCESS";

	public static final String FAILURE="FAILURE";

	private String code;

	private String message;

	private Object content;

}
