package com.softvision.digital.pms.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionUtil {

	private ExceptionUtil() {}

	public static String getExceptionMessage(Throwable exception) {
		String message = exception.getMessage();
		Throwable cause = exception.getCause();
		log.info("Exception Class={}, message={}", exception.getClass().getName(), message);
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
