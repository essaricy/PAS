package com.softvision.ipm.pms.assign.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.assign.constant.AssignmentPhaseStatus;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class ManagerAssignmentRepository extends AbstractRepository {

	private static final String QUERY_MANAGER_ASSIGNMENTS ="select  " + 
			"emp_phase_assign.id as id, " +
			"emp_phase_assign.status as status, " + 
			"appr_phase.id as appr_phase_id, " + 
			"appr_phase.name as appr_phase_name, " + 
			"emp_phase_assign.assigned_at as assigned_at, " + 
			"emp_phase_assign.employee_id as assigned_to_id, " + 
			"(select first_name || ' ' || last_name from employee where employee_id=emp_phase_assign.employee_id) as assigned_to_name, " + 
			"emp_phase_assign.assigned_by as assigned_by_id, " + 
			"(select first_name || ' ' || last_name from employee where employee_id=emp_phase_assign.assigned_by) as assigned_by_name " + 
			"from emp_phase_assign " + 
			"inner join appr_phase " + 
			"on appr_phase.id=emp_phase_assign.phase_id"
			+ " where emp_phase_assign.assigned_by=?";

	public List<EmployeeAssignmentDto> getCurrentPhases(int employeeId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				QUERY_MANAGER_ASSIGNMENTS,
			    new Object[] {employeeId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			        	EmployeeAssignmentDto employeeAssignment = new EmployeeAssignmentDto();
			        	employeeAssignment.setAssignmentId(rs.getLong("id"));
			        	employeeAssignment.setPhaseId(rs.getInt("appr_phase_id"));
			        	employeeAssignment.setPhaseName(rs.getString("appr_phase_name"));
			        	employeeAssignment.setAssignedAt(rs.getDate("assigned_at"));
			        	employeeAssignment.setAssignedToId(rs.getInt("assigned_to_id"));
			        	employeeAssignment.setAssignedToName(rs.getString("assigned_to_name"));
			        	employeeAssignment.setAssignedById(rs.getInt("assigned_by_id"));
			        	employeeAssignment.setAssignedByName(rs.getString("assigned_by_name"));
			        	employeeAssignment.setStatus(AssignmentPhaseStatus.get(rs.getInt("status")));
			            return employeeAssignment;
			        }
			    });
		return list;
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
