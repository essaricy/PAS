package com.softvision.ipm.pms.assign.repo;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;

public class AssignmentRepositorySql {

	public static final String SELECT_CYCLE_ASSIGNMENTS ="select " + 
			"cycle_assign.id as id, " + 
			"cycle_assign.status as status, " + 
			"appr_cycle.id as appr_cycle_id, " + 
			"appr_cycle.name as appr_cycle_name, " + 
			"cycle_assign.assigned_at as assigned_at, " + 
			"cycle_assign.employee_id as assigned_to_id, " + 
			"(select first_name from employee where employee_id=cycle_assign.employee_id) as assigned_to_first_name, " + 
			"(select last_name from employee where employee_id=cycle_assign.employee_id) as assigned_to_last_name, " + 
			"cycle_assign.assigned_by as assigned_by_id, " + 
			"(select first_name from employee where employee_id=cycle_assign.assigned_by) as assigned_by_first_name, " + 
			"(select last_name from employee where employee_id=cycle_assign.assigned_by) as assigned_by_last_name, " + 
			"cycle_assign.score as score " +
			"from cycle_assign " + 
			"inner join appr_cycle " + 
			"on appr_cycle.id=cycle_assign.cycle_id ";

	public static final String SELECT_CYCLE_ASSIGNMENTS_ASSIGNED_TO = SELECT_CYCLE_ASSIGNMENTS +
			"where cycle_assign.employee_id=? " + 
		 	"and appr_cycle.id=? ";

	public static final String SELECT_CYCLE_ASSIGNMENTS_ASSIGNED_BY = SELECT_CYCLE_ASSIGNMENTS +
			"where cycle_assign.assigned_by=? " + 
		 	"and appr_cycle.id=? " +
		 	"order by cycle_assign.employee_id asc ";

	public static final String SELECT_CYCLE_ASSIGNMENTS_SUBMITTED_TO = SELECT_CYCLE_ASSIGNMENTS +
			"where cycle_assign.submitted_to=? " + 
		 	"and appr_cycle.id=? " +
		 	"order by cycle_assign.employee_id asc "; 

	public static final String SELECT_MANAGER_ALL_CYCLE_ASSIGNMENTS = SELECT_CYCLE_ASSIGNMENTS + 
			"where appr_cycle.id=? " + 
			"order by appr_cycle.start_date asc, cycle_assign.employee_id asc ";

	public static final String SELECT_PHASE_ASSIGNMENTS ="select  " + 
			"phase_assign.id as id, " +
			"phase_assign.status as status, " + 
			"appr_phase.id as appr_phase_id, " + 
			"appr_phase.name as appr_phase_name, " + 
			"template.id as template_id, " +
			"template.name as template_name, " +
			"phase_assign.assigned_at as assigned_at, " + 
			"phase_assign.employee_id as assigned_to_id, " + 
			"(select first_name from employee where employee_id=phase_assign.employee_id) as assigned_to_first_name, " +
			"(select last_name from employee where employee_id=phase_assign.employee_id) as assigned_to_last_name, " +
			"phase_assign.assigned_by as assigned_by_id, " + 
			"(select first_name from employee where employee_id=phase_assign.assigned_by) as assigned_by_first_name, " +
			"(select last_name from employee where employee_id=phase_assign.assigned_by) as assigned_by_last_name, " +
			"phase_assign.score as score " +
			"from phase_assign " + 
			"inner join appr_phase " + 
			"on appr_phase.id=phase_assign.phase_id " +
			"inner join template " +
			"on template.id=phase_assign.template_id ";

	public static final String SELECT_PHASE_ASSIGNMENTS_ASSIGNED_BY = SELECT_PHASE_ASSIGNMENTS + 
			"where phase_assign.assigned_by=? " + 
			"and appr_phase.cycle_id=? " + 
			"and appr_phase.id=? " + 
			"order by phase_assign.employee_id asc";

	public static final String SELECT_PHASE_ASSIGNMENTS_ASSIGNED_TO = SELECT_PHASE_ASSIGNMENTS + 
			"where phase_assign.employee_id=? " + 
			"and appr_phase.cycle_id=? " + 
			"and appr_phase.id=? ";

	public static final String SELECT_PHASE_ASSIGNMENTS_BY_CYCLE = /*"select appr_phase.name as appr_phase_name, " + 
	        "appr_phase.start_date as appr_phase_start_date, " + 
	        "appr_phase.end_date as appr_phase_end_date, " + 
	        "phase_assign.id as assign_id, " + 
	        "phase_assign.employee_id as phase_assign_employee_id, " + 
	        "phase_assign.assigned_by as phase_assign_assigned_by, " + 
	        "phase_assign.status as phase_assign_status, " + 
	        "phase_assign.score as phase_assign_score " + 
	        "from phase_assign " + 
	        "inner join appr_phase " + 
	        "on appr_phase.id=phase_assign.phase_id " + 
	        "inner join appr_cycle " + 
	        "on appr_cycle.id=appr_phase.cycle_id " + 
	        "where " + 
	        "appr_cycle.id=? " + 
	        "and phase_assign.employee_id=? " + 
	        "order by appr_phase.start_date asc "*/SELECT_PHASE_ASSIGNMENTS +
	        "inner join appr_cycle " + 
            "on appr_cycle.id=appr_phase.cycle_id " + 
	        "where " + 
            "appr_cycle.id=? " + 
            "and phase_assign.employee_id=? " + 
            "order by appr_phase.start_date asc ";
	        ; 

	public static final String SELECT_INCOMPLETE_PREVIOUS_PHASE_ASSIGNMENTS = SELECT_PHASE_ASSIGNMENTS + 
			"where " +
			"phase_assign.id != ? " +
			"and employee_id=? " + 
			"and appr_phase.start_date < (select start_date from appr_phase where id=?)" + 
			"and status != " + PhaseAssignmentStatus.CONCLUDED.getCode() +
			//" appr_phase.cycle_id=(select cycle_id from appr_phase where id=phase_assign.phase_id) " +
			"order by appr_phase.start_date " + 
			"LIMIT 1 ";

	public static final String SELECT_MANAGER_ALL_PHASE_ASSIGNMENTS = SELECT_PHASE_ASSIGNMENTS + 
			"where appr_phase.cycle_id=? " + 
			"and appr_phase.id=? ";


}
