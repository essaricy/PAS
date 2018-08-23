package com.softvision.digital.pms.assign.util;

import java.text.MessageFormat;
import java.util.Arrays;

import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.common.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssignmentUtil {

	private static final String ERROR_MESSAGE = "Cannot change the status from \"{0}\" to \"{1}\"";

	private AssignmentUtil() {}

	public static PhaseAssignmentStatus validateStatus(int status, String desiredState,
			PhaseAssignmentStatus... statusesToAllow) throws ServiceException {
		PhaseAssignmentStatus currentStatus = PhaseAssignmentStatus.get(status);
		boolean found = Arrays.stream(statusesToAllow).anyMatch(o -> o==currentStatus);
		if (!found) {
			String message = MessageFormat.format(ERROR_MESSAGE, currentStatus.getName(), desiredState);
			log.error(message);
			throw new ServiceException(message);
		}
		return PhaseAssignmentStatus.getNext(status);
	}

}
