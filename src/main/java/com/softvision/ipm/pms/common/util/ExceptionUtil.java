package com.softvision.ipm.pms.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softvision.ipm.pms.appraisal.service.AppraisalService;

public class ExceptionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppraisalService.class);

	public static String getExceptionMessage(Throwable exception) {
		String message = exception.getMessage();
		Throwable cause = exception.getCause();
		LOGGER.info("Exception Class={}, message={}", exception.getClass().getName(), message);
		if (cause == null) {
			if (message != null) {
				if (message.contains("already exists")) {
					return message.substring(message.lastIndexOf("Detail:") + "Detail:".length(), message.length());
				} else {
					return message;
				}
			}
		} else {
			return getExceptionMessage(cause);
		}
		return message;
	}

}
