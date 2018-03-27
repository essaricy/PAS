package com.softvision.ipm.pms.assign.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.common.exception.ServiceException;

public class AssignmentUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentUtil.class);

	private static final String ERROR_MESSAGE = "The Appraisal form is not in a state to {0} now";

	public static PhaseAssignmentStatus validateStatus(int status, String desiredState,
			PhaseAssignmentStatus... statusesToCheck) throws ServiceException {
		boolean found=false;
		PhaseAssignmentStatus currentStatus = PhaseAssignmentStatus.get(status);
		for (PhaseAssignmentStatus statusToCheck : statusesToCheck) {
			if (currentStatus == statusToCheck) {
				found = true;
				break;
			}
		}
		if (!found) {
			String message = MessageFormat.format(ERROR_MESSAGE, desiredState);
			LOGGER.warn(message);
			throw new ServiceException(message);
		}
		return PhaseAssignmentStatus.getNext(status);
	}

}
