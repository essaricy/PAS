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
public class ManagerAssignmentRepository extends AbstractRepository {

	public List<EmployeeAssignmentDto> getAssignedByAssignmentsOfCycle(int employeeId, int cycleId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.QUERY_MANAGER_CYCLE_ASSIGNMENTS,
			    new Object[] {employeeId, cycleId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssember.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}

	public List<EmployeeAssignmentDto> getAssignedByAssignmentsOfPhase(int employeeId, int cycleId, int phaseId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_PHASE_ASSIGNMENTS_ASSIGNED_BY,
			    new Object[] {employeeId, cycleId, phaseId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssember.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}

	public EmployeeAssignmentDto getPreviousIncompletePhaseAssignment(long phaseAssignId, int employeeId, int phaseId) {
		List<EmployeeAssignmentDto> employeeAssignments = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_INCOMPLETE_PREVIOUS_PHASE_ASSIGNMENTS,
			    new Object[] {phaseAssignId, employeeId, phaseId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssember.getEmployeeAssignment(rs);
			        }
			    });
		return (employeeAssignments== null || employeeAssignments.isEmpty()) ? null : employeeAssignments.get(0);
	}

	public boolean changeManager(Long phaseAssignId, int toEmployeeId) {
		int updated = jdbcTemplate.update("UPDATE emp_phase_assign SET assigned_by=? where id=?", toEmployeeId, phaseAssignId);
		return updated==1;
	}

	public boolean changeStatus(long phaseAssignId, int toStatusId) {
		int updated = jdbcTemplate.update("UPDATE emp_phase_assign SET status=? where id=?", toStatusId, phaseAssignId);
		return updated==1;
	}

}
