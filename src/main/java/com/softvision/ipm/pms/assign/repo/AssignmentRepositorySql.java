package com.softvision.ipm.pms.assign.repo;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;

public class AssignmentRepositorySql {

	public static final String QUERY_MANAGER_CYCLE_ASSIGNMENTS ="select" + 
			"    emp_cycle_assign.id as id," + 
			"    emp_cycle_assign.status as status, " + 
			"    appr_cycle.id as appr_cycle_id, " + 
			"    appr_cycle.name as appr_cycle_name, " + 
			"    emp_cycle_assign.assigned_at as assigned_at, " + 
			"    emp_cycle_assign.employee_id as assigned_to_id, " + 
			"    (select first_name from employee where employee_id=emp_cycle_assign.employee_id) as assigned_to_first_name," + 
			"    (select last_name from employee where employee_id=emp_cycle_assign.employee_id) as assigned_to_last_name," + 
			"    emp_cycle_assign.assigned_by as assigned_by_id, " + 
			"    (select first_name from employee where employee_id=emp_cycle_assign.assigned_by) as assigned_by_first_name," + 
			"    (select last_name from employee where employee_id=emp_cycle_assign.assigned_by) as assigned_by_last_name " + 
			"    from emp_cycle_assign " + 
			"    inner join appr_cycle " + 
			"    on appr_cycle.id=emp_cycle_assign.cycle_id " + 
			"     where emp_cycle_assign.assigned_by=? " + 
			"    and appr_cycle.id=? " + 
			"    order by appr_cycle.start_date asc, status desc, emp_cycle_assign.employee_id asc " + 
			"";

	public static final String SELECT_PHASE_ASSIGNMENTS ="select  " + 
			"emp_phase_assign.id as id, " +
			"emp_phase_assign.status as status, " + 
			"appr_phase.id as appr_phase_id, " + 
			"appr_phase.name as appr_phase_name, " + 
			"emp_phase_assign.assigned_at as assigned_at, " + 
			"emp_phase_assign.employee_id as assigned_to_id, " + 
			"(select first_name from employee where employee_id=emp_phase_assign.employee_id) as assigned_to_first_name, " +
			"(select last_name from employee where employee_id=emp_phase_assign.employee_id) as assigned_to_last_name, " +
			"emp_phase_assign.assigned_by as assigned_by_id, " + 
			"(select first_name from employee where employee_id=emp_phase_assign.assigned_by) as assigned_by_first_name, " +
			"(select last_name from employee where employee_id=emp_phase_assign.assigned_by) as assigned_by_last_name " +
			"from emp_phase_assign " + 
			"inner join appr_phase " + 
			"on appr_phase.id=emp_phase_assign.phase_id";

	public static final String SELECT_PHASE_ASSIGNMENTS_ASSIGNED_BY = SELECT_PHASE_ASSIGNMENTS + 
			" where emp_phase_assign.assigned_by=? " + 
			"and appr_phase.cycle_id=? " + 
			"and appr_phase.id=? " + 
			"order by appr_phase.start_date asc, status desc, emp_phase_assign.employee_id asc";

	public static final String SELECT_PHASE_ASSIGNMENTS_ASSIGNED_TO = SELECT_PHASE_ASSIGNMENTS + 
			" where emp_phase_assign.employee_id=? " + 
			"and appr_phase.cycle_id=? " + 
			"and appr_phase.id=? ";

	public static final String SELECT_INCOMPLETE_PREVIOUS_PHASE_ASSIGNMENTS = SELECT_PHASE_ASSIGNMENTS + 
			" where " +
			" emp_phase_assign.id != ? " +
			" and employee_id=? " + 
			" and appr_phase.start_date < (select start_date from appr_phase where id=?)" + 
			" and status != " + PhaseAssignmentStatus.CONCLUDED.getCode() +
			//" appr_phase.cycle_id=(select cycle_id from appr_phase where id=emp_phase_assign.phase_id) " +
			" order by appr_phase.start_date " + 
			" LIMIT 1 ";

}
