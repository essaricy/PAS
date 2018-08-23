package com.softvision.digital.pms.common.util;

import com.softvision.digital.pms.common.model.Result;

public class ResultUtil {

	private ResultUtil() {}

	public static Result getSuccess(String message) {
		return getSuccess(message, null);
	}

	public static Result getSuccess(String message, Object content) {
		Result result = new Result();
		result.setCode(Result.SUCCESS);
		result.setMessage(message);
		result.setContent(content);
		return result;
	}

	public static Result getFailure(Exception exception) {
		Result result = new Result();
		result.setCode(Result.FAILURE);
		result.setMessage(exception.getMessage());
		return result;
	}

}
