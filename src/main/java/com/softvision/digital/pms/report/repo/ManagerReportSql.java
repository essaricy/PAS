package com.softvision.digital.pms.report.repo;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;

public class ManagerReportSql {

	private ManagerReportSql() {}

	public static final String SELECT_PHASEWISE_EMPLOYEE_STATUS_COUNT = "select "
			+ "phase_assign.status as status, "
			+ "count(phase_assign.id) as count "
			+ "from phase_assign "
			+ "inner join appr_phase on phase_assign.phase_id=appr_phase.id "
			+ "inner join appr_cycle on appr_cycle.id=appr_phase.cycle_id "
			+ "where "
			+ "appr_cycle.status = '" + AppraisalCycleStatus.ACTIVE.toString() + "' "
			+ "and phase_assign.assigned_by=? "
			+ "group by phase_assign.status "
			+ "order by phase_assign.status asc";

}
