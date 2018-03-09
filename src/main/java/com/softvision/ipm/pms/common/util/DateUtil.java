package com.softvision.ipm.pms.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	public static final String getIndianDateFormat(Date date) {
		return FORMAT.format(date);
	}

	public static Date parseIndianFormatDate(String time) throws ParseException {
		return FORMAT.parse(time);
	}
}
