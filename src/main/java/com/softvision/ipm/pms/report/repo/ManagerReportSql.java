package com.softvision.ipm.pms.report.repo;

public class ManagerReportSql {

	public static final String SELECT_PHASEWISE_EMPLOYEE_STATUS_COUNT = "select "
			+ "status as status, "
			+ "count(*) as count "
			+ "from emp_phase_assign "
			+ "where assigned_by=? "
			+ "group by status "
			+ "order by status asc";

}
