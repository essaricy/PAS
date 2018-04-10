package com.softvision.ipm.pms.assign.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.assign.assembler.AssignmentSqlAssembler;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class EmployeeAssignmentRepository extends AbstractRepository {

	public EmployeeAssignmentDto getEmployeeAssignmentInCycle(int employeeId, int cycleId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_CYCLE_ASSIGNMENTS_ASSIGNED_TO,
			    new Object[] {employeeId, cycleId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssembler.getEmployeeAssignment(rs);
			        }
			    });
		return (list == null || list.isEmpty())? null : list.get(0);
	}
	
	public List<EmployeeAssignmentDto> getEmployeeAssignmentsOfPhase(int employeeId, int cycleId, int phaseId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_PHASE_ASSIGNMENTS_ASSIGNED_TO,
			    new Object[] {employeeId, cycleId, phaseId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssembler.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}

}
