package com.softvision.ipm.pms.assign.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.ipm.pms.assign.model.PhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class ManagerPhaseAssignmentService {

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private RoleService roleService;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	public void assignPhaseToAnotherManager(long phaseAssignId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(phaseAssignId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		// he must be the same employee who has been assigned
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		if (assignedBy != fromEmployeeId) {
			throw new ServiceException("The manager who assigned can only enable the form");
		}
		int status = employeePhaseAssignment.getStatus();
		PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(status);
		if (status > PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.getCode()) {
			throw new ServiceException("Assignment is in '" + phaseAssignmentStatus.getName() + "' status. Cannot change the manager now");
		}
		if (!roleService.isManager(toEmployeeId)) {
			throw new ServiceException("The employee that you are trying to assign to, is not a manager");
		}
		// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changePhaseManager(phaseAssignId, toEmployeeId);
		if (!changed) {
			throw new ServiceException("Unable to change assigned-by-manager");
		}
	}

	public void enablePhaseAppraisal(long phaseAssignId, int fromEmployeeId) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(phaseAssignId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		// he must be the same employee who has been assigned
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		if (assignedBy != fromEmployeeId) {
			throw new ServiceException("The manager who assigned can only enable the form");
		}
		int existingStatusId = employeePhaseAssignment.getStatus();
		PhaseAssignmentStatus existingStatus = PhaseAssignmentStatus.get(existingStatusId);

		if (existingStatus != PhaseAssignmentStatus.NOT_INITIATED) {
			throw new ServiceException("The Appraisal Phase is not yet initiated for you.");
		}
		int employeeId = employeePhaseAssignment.getEmployeeId();
		// check if any previous assignments are pending.
		EmployeeAssignmentDto incompletePhaseAssignment = managerAssignmentRepository.getPreviousIncompletePhaseAssignment(phaseAssignId, employeeId, employeePhaseAssignment.getPhaseId());
		if (incompletePhaseAssignment != null) {
			throw new ServiceException("Make sure that assignments in previous phase are concluded for this employee before assigning new one");
		}
		// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changeStatus(phaseAssignId, PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.getCode());
		if (!changed) {
			throw new ServiceException("Unable to change assigned-by-manager");
		}
		// TODO email trigger
	}

	public List<ManagerCycleAssignmentDto> getAllCycles(int employeeId) {
		List<ManagerCycleAssignmentDto> cycleAssignments = new ArrayList<>();
		List<AppraisalCycle> allCycles = appraisalCycleDataRepository.findAllByOrderByStartDateDesc();
		for (AppraisalCycle cycle : allCycles) {
			int cycleId = cycle.getId();
			ManagerCycleAssignmentDto cycleAssignment = new ManagerCycleAssignmentDto();
			cycleAssignment.setCycle(AppraisalAssembler.getCycle(cycle));
			cycleAssignment.setEmployeeAssignments(managerAssignmentRepository.getAssignedByAssignmentsOfCycle(employeeId, cycleId));
			List<PhaseAssignmentDto> phaseAssignments = new ArrayList<>();
			cycleAssignment.setPhaseAssignments(phaseAssignments);
			List<AppraisalPhase> phases = cycle.getPhases();
			for (AppraisalPhase phase : phases) {
				PhaseAssignmentDto phaseAssignment = new PhaseAssignmentDto();
				phaseAssignment.setPhase(AppraisalAssembler.getPhase(phase));
				phaseAssignment.setEmployeeAssignments(managerAssignmentRepository.getAssignedByAssignmentsOfPhase(employeeId, cycleId, phase.getId()));
				phaseAssignments.add(phaseAssignment);
			}
			cycleAssignments.add(cycleAssignment);
		}
		return cycleAssignments;
	}

}
