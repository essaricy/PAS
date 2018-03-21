package com.softvision.ipm.pms.assign.util;

import java.text.MessageFormat;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.common.exception.ServiceException;

public class AssignmentUtil {

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
			throw new ServiceException(MessageFormat.format(ERROR_MESSAGE, desiredState));
		}
		return PhaseAssignmentStatus.getNext(status);
	}

}
