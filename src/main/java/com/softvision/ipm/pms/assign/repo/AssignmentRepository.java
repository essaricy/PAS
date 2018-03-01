package com.softvision.ipm.pms.assign.repo;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class AssignmentRepository extends AbstractRepository {

	@Autowired private AssignmentPhaseDataRepository assignmentPhaseDataRepository;

	@Transactional
	public void assign(EmployeeCycleAssignment cycleAssignment, AppraisalCycle cycle, String employeeName)
			throws ServiceException {

		Date assignedAt = cycleAssignment.getAssignedAt();
		int assignedBy = cycleAssignment.getAssignedBy();
		int employeeId = cycleAssignment.getEmployeeId();
		long templateId = cycleAssignment.getTemplateId();
		int cycleId = cycleAssignment.getCycleId();

		int inserted = jdbcTemplate.update(
				"INSERT INTO emp_cycle_assign("
						+ "ID, CYCLE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
						+ "VALUES(nextval('emp_cycle_assign_id_seq'), ?, ?, ?, ?, ?)",
						cycleId, templateId, employeeId, assignedBy, assignedAt);
		System.out.println("Cycle inserted? " + inserted);
		if (inserted!=1) {
			throw new ServiceException("Employee (" + employeeName + ") - Unable to assign appraisal cycle ");
		}
		List<AppraisalPhase> phases = cycle.getPhases();
		for (AppraisalPhase appraisalPhase : phases) {
			Long phaseId = appraisalPhase.getId();
			System.out.println("Doing for Phase " + phaseId);
			EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findByPhaseIdAndTemplateIdAndEmployeeId(phaseId.intValue(), templateId, employeeId);
			if (employeePhaseAssignment != null) {
				throw new ServiceException("Employee (" + employeeId + ") - An appraisal phase has already been assigned by within cycle");
			}
			System.out.println("vemployeePhaseAssignment = " + employeePhaseAssignment);
			inserted = jdbcTemplate.update(
					"INSERT INTO emp_phase_assign("
							+ "ID, PHASE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
							+ "VALUES(nextval('emp_phase_assign_id_seq'), ?, ?, ?, ?, ?)",
							phaseId, templateId, employeeId, assignedBy, assignedAt);
			System.out.println("Phase inserted? " + inserted);
			if (inserted != 1) {
				throw new ServiceException("Employee (" + employeeName + ") - Unable to assign appraisal phase ");
			}
		}
	}

	/*public boolean assignPhases(List<EmployeePhaseAssignment> phaseAssignments) throws ServiceException {
		for (EmployeePhaseAssignment phaseAssignment : phaseAssignments) {
			Date assignedAt = phaseAssignment.getAssignedAt();
			int assignedBy = phaseAssignment.getAssignedBy();
			int employeeId = phaseAssignment.getEmployeeId();
			int phaseId = phaseAssignment.getPhaseId();
			long templateId = phaseAssignment.getTemplateId();

			EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findByPhaseIdAndTemplateIdAndEmployeeId(phaseId, templateId, employeeId);
			if (employeePhaseAssignment != null) {
				throw new ServiceException("Employee (" + employeeId + ") - An appraisal phase has already been assigned by within cycle");
			}
			int inserted = jdbcTemplate.update(
					"INSERT INTO emp_phase_assign("
							+ "ID, PHASE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
							+ "VALUES(nextval('emp_phase_assign_id_seq'), ?, ?, ?, ?, ?)",
							phaseId, templateId, employeeId, assignedBy, assignedAt);
			if (inserted != 1) {
				throw new ServiceException("Employee (" + employeeId + ") - Unable to assign appraisal phase ");
			}
		}
		return true;
	}

	public boolean assignPhases(EmployeePhaseAssignment phaseAssignment) {
		List<AppraisalPhase> phases = cycle.getPhases();
		for (AppraisalPhase phase : phases) {
			cycleAssignment.getTemplateId();
			assignmentPhaseDataRepository.findByPhaseIdAndTemplateIdAndEmployeeId(phaseId, templateId, employeeId)
			int inserted = jdbcTemplate.update(
					"INSERT INTO emp_phase_assign("
							+ "ID, PHASE_ID, TEMPLATE_ID, EMPLOYEE_ID, ASSIGNED_BY, ASSIGNED_AT) "
							+ "VALUES(nextval('emp_phase_assign_id_seq'), ?, ?, ?, ?, ?)",
							cycleAssignment.getCycleId(), cycleAssignment.getEmployeeId(),
							cycleAssignment.getAssignedBy(), cycleAssignment.getAssignedAt());
		}
		return true;
	}*/

}
