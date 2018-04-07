package com.softvision.ipm.pms.assign.util;

import java.text.MessageFormat;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.common.exception.ServiceException;

public class AssignmentUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentUtil.class);

	private static final String ERROR_MESSAGE = "Cannot change the status from {0} to {1}";

	public static PhaseAssignmentStatus validateStatus(int status, String desiredState,
			PhaseAssignmentStatus... statusesToCheck) throws ServiceException {
		PhaseAssignmentStatus currentStatus = PhaseAssignmentStatus.get(status);
		boolean found = Arrays.stream(statusesToCheck).anyMatch(o -> o==currentStatus);
		if (!found) {
			String message = MessageFormat.format(ERROR_MESSAGE, currentStatus, desiredState);
			LOGGER.error(message);
			throw new ServiceException(message);
		}
		return PhaseAssignmentStatus.getNext(status);
	}

}
