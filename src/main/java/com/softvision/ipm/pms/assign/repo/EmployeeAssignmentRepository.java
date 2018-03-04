package com.softvision.ipm.pms.assign.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.assign.assembler.AssignmentSqlAssember;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class EmployeeAssignmentRepository extends AbstractRepository {

	private static final String QUERY_MANAGER_CYCLE_ASSIGNMENTS ="select" + 
			"    emp_cycle_assign.id as id," + 
			"    emp_cycle_assign.status as status, " + 
			"    appr_cycle.id as appr_cycle_id, " + 
			"    appr_cycle.name as appr_cycle_name, " + 
			"    emp_cycle_assign.assigned_at as assigned_at, " + 
			"    emp_cycle_assign.employee_id as assigned_to_id,\r\n" + 
			"    (select first_name from employee where employee_id=emp_cycle_assign.employee_id) as assigned_to_first_name," + 
			"    (select last_name from employee where employee_id=emp_cycle_assign.employee_id) as assigned_to_last_name," + 
			"    emp_cycle_assign.assigned_by as assigned_by_id, " + 
			"    (select first_name from employee where employee_id=emp_cycle_assign.assigned_by) as assigned_by_first_name," + 
			"    (select last_name from employee where employee_id=emp_cycle_assign.assigned_by) as assigned_by_last_name " + 
			"    from emp_cycle_assign " + 
			"    inner join appr_cycle " + 
			"    on appr_cycle.id=emp_cycle_assign.cycle_id " + 
			"     where emp_cycle_assign.employee_id=? " + 
			"    and appr_cycle.id=? ";

	private static final String QUERY_MANAGER_PHASE_ASSIGNMENTS ="select  " + 
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
			"on appr_phase.id=emp_phase_assign.phase_id" + 
			" where emp_phase_assign.employee_id=? " + 
			"and appr_phase.cycle_id=? " + 
			"and appr_phase.id=? ";

	public List<EmployeeAssignmentDto> getEmployeeAssignmentsOfCycle(int employeeId, int cycleId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				QUERY_MANAGER_CYCLE_ASSIGNMENTS,
			    new Object[] {employeeId, cycleId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssember.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}

	public List<EmployeeAssignmentDto> getEmployeeByAssignmentsOfPhase(int employeeId, int cycleId, int phaseId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				QUERY_MANAGER_PHASE_ASSIGNMENTS,
			    new Object[] {employeeId, cycleId, phaseId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssember.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}

}
