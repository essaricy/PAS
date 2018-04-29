package com.softvision.ipm.pms.data;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.employee.constant.EmployeeOrg;
import com.softvision.ipm.pms.employee.constant.EmploymentType;

public class MainTest {

	public static void main(String[] args) throws ServiceException {
	    EmploymentType employmentType = EmploymentType.PROJECT_EMPLOYEE;
        EmployeeOrg employeeOrg = EmployeeOrg.IT;

        if (employmentType != EmploymentType.REGULAR_EMPLOYEE && employmentType != EmploymentType.PROJECT_EMPLOYEE) {
            throw new ServiceException("Not Allowed 1");
        }

        if (employmentType == EmploymentType.PROJECT_EMPLOYEE && employeeOrg != EmployeeOrg.IT) {
            throw new ServiceException("Not Allowed 2");
        }
	}

}
