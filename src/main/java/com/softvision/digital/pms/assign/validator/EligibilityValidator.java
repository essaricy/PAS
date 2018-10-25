package com.softvision.digital.pms.assign.validator;

import java.text.MessageFormat;
import java.util.Date;

import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.util.DateUtil;
import com.softvision.digital.pms.employee.constant.EmployeeLocation;
import com.softvision.digital.pms.employee.constant.EmployeeOrg;
import com.softvision.digital.pms.employee.constant.EmploymentType;
import com.softvision.digital.pms.employee.entity.Employee;

public class EligibilityValidator {

	private EligibilityValidator() {}

	private static final String NOT_ELIGIBLE_BY_CUTOFF_DATE = "{0} - Not eligible for this appraisal. He/She has joined on {1} which is after the appraisal eligibility cut off date ({2})";

	private static final String NOT_ELIGIBLE_BY_EMPLOYEE_TYPE = "{0} - Not eligible for this appraisal. His/Her employement type is {1}";

	private static final String NOT_ELIGIBLE_BY_EMPLOYEE_TYPE_ORG = "{0} - Not eligible for this appraisal. His/Her employement type is {1} and org is {2}";

	private static final String NOT_ELIGIBLE_BY_LOCATION = "{0} - Not eligible for this appraisal. His/Her location is {1}";

	public static void checkEligibility(Employee employee, AppraisalCycle cycle)
			throws ServiceException {
		checkEligibility(employee, cycle.getCutoffDate());
	}

	public static void checkEligibility(Employee employee, Date cutoffDate)
			throws ServiceException {
		String employeeName = employee.getFirstName() + " " + employee.getLastName();
		EmployeeLocation employeeLocation = EmployeeLocation.get(employee.getLocation());
		if (employeeLocation != EmployeeLocation.INDIA && employeeLocation != EmployeeLocation.AUSTRALIA) {
		    throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_LOCATION, employeeName, employee.getLocation()));
		}
		EmployeeOrg employeeOrg = EmployeeOrg.get(employee.getOrg());
		EmploymentType employmentType = EmploymentType.get(employee.getEmploymentType());
		if (employmentType != EmploymentType.REGULAR_EMPLOYEE && employmentType != EmploymentType.PROJECT_EMPLOYEE
				&& employmentType != EmploymentType.CONFIRMED && employmentType != EmploymentType.PROBATIONARY) {
		    throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_EMPLOYEE_TYPE, employeeName,
		            (employmentType == null) ? "UNKNOWN" : employmentType.getName()));
		}
		if (employmentType == EmploymentType.PROJECT_EMPLOYEE && (employeeOrg != EmployeeOrg.IT && employeeOrg != EmployeeOrg.BPO)) {
		    throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_EMPLOYEE_TYPE_ORG, employeeName,
		            (employmentType == null) ? "UNKNOWN" : employmentType.getName(), employeeOrg));
		}
		Date hiredOn = employee.getHiredOn();
		// check if employee hired date should be before cutoff date
		if (hiredOn.after(cutoffDate)) {
		    throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_CUTOFF_DATE, employeeName,
		            DateUtil.getIndianDateFormat(hiredOn), DateUtil.getIndianDateFormat(cutoffDate)));
		}
	}

}
