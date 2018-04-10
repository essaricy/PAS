package com.softvision.ipm.pms.assign.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.assign.assembler.AssignmentSqlAssembler;
import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class AssignmentRepository extends AbstractRepository {

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Transactional
	public void assign(CycleAssignment cycleAssignment, AppraisalCycle cycle, String employeeName)
			throws ServiceException {

		Date assignedAt = cycleAssignment.getAssignedAt();
		int assignedBy = cycleAssignment.getAssignedBy();
		int employeeId = cycleAssignment.getEmployeeId();
		long templateId = cycleAssignment.getTemplateId();
		int cycleId = cycleAssignment.getCycleId();

		int inserted = jdbcTemplate.update(
				"INSERT INTO cycle_assign("
						+ "ID, CYCLE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
						+ "VALUES(nextval('cycle_assign_id_seq'), ?, ?, ?, ?, ?)",
						cycleId, templateId, employeeId, assignedBy, assignedAt);
		if (inserted!=1) {
			throw new ServiceException("Employee (" + employeeName + ") - Unable to assign appraisal cycle ");
		}
		List<AppraisalPhase> phases = cycle.getPhases();
		for (AppraisalPhase appraisalPhase : phases) {
			Integer phaseId = appraisalPhase.getId();
			PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeIdAndTemplateId(phaseId.intValue(), employeeId, templateId);
			if (employeePhaseAssignment != null) {
				throw new ServiceException("Employee (" + employeeId + ") - An appraisal phase has already been assigned by within cycle");
			}
			inserted = jdbcTemplate.update(
					"INSERT INTO phase_assign("
							+ "ID, PHASE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
							+ "VALUES(nextval('phase_assign_id_seq'), ?, ?, ?, ?, ?)",
							phaseId, templateId, employeeId, assignedBy, assignedAt);
			if (inserted != 1) {
				throw new ServiceException("Employee (" + employeeName + ") - Unable to assign appraisal phase ");
			}
		}
	}

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

	public List<EmployeeAssignmentDto> getAllEmployeeAssignmentsforCycle(int cycleId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_MANAGER_ALL_CYCLE_ASSIGNMENTS,
			    new Object[] {cycleId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssembler.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}
	
	public List<EmployeeAssignmentDto> getAllEmployeeAssignmentsforPhase(int cycleId, int phaseId) {
		List<EmployeeAssignmentDto> list = jdbcTemplate.query(
				AssignmentRepositorySql.SELECT_MANAGER_ALL_PHASE_ASSIGNMENTS,
			    new Object[] {cycleId, phaseId},
			    new RowMapper<EmployeeAssignmentDto>() {
			        public EmployeeAssignmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			            return AssignmentSqlAssembler.getEmployeeAssignment(rs);
			        }
			    });
		return list;
	}

}
