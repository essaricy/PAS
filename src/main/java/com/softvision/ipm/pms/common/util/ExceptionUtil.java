package com.softvision.ipm.pms.common.util;

public class ExceptionUtil {

	public static String getExcceptionMessage(Throwable exception) {
		String message = exception.getMessage();
		Throwable cause = exception.getCause();
		System.out.println("Exception Class=" + exception.getClass().getName() + ", message=" + message);
		if (cause == null) {
			if (message != null) {
				if (message.contains("already exists")) {
					return message.substring(message.lastIndexOf("Detail:") + "Detail:".length(), message.length());
				} else {
					return message;
				}
			}
		} else {
			return getExcceptionMessage(cause);
		}
		return message;
	}

}
