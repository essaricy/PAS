package com.softvision.ipm.pms.assign.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.assign.assembler.AssignmentSqlAssembler;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class AssignmentRepository extends AbstractRepository {

	@Transactional
	public void assign(PhaseAssignment phaseAssignment, AppraisalPhase phase, String employeeName)
			throws ServiceException {

		Date assignedAt = phaseAssignment.getAssignedAt();
		int assignedBy = phaseAssignment.getAssignedBy();
		int employeeId = phaseAssignment.getEmployeeId();
		long templateId = phaseAssignment.getTemplateId();
		int phaseId = phaseAssignment.getPhaseId();

		int inserted = jdbcTemplate.update(
				"INSERT INTO phase_assign("
						+ "ID, PHASE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
						+ "VALUES(nextval('phase_assign_id_seq'), ?, ?, ?, ?, ?)",
						phaseId, templateId, employeeId, assignedBy, assignedAt);
		if (inserted!=1) {
			throw new ServiceException("Employee (" + employeeName + ") - Unable to assign appraisal phase ");
		}
	}

	public List<EmployeeAssignmentDto> getAllAssignments(int cycleId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_MANAGER_ALL_CYCLE_ASSIGNMENTS,
			    new Object[] {cycleId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssembler.getEmployeeAssignment_Cycle(rs);
			        }
			    });
		return list;
	}
	
	public List<EmployeePhaseAssignmentDto> getAllAssignments(int cycleId, int phaseId) {
		List<EmployeePhaseAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_MANAGER_ALL_PHASE_ASSIGNMENTS,
			    new Object[] {cycleId, phaseId},
			    new RowMapper<EmployeePhaseAssignmentDto>() {
			        public EmployeePhaseAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssembler.getEmployeePhaseAssignment(rs);
			        }
			    });
		return list;
	}

}
