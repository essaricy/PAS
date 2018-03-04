package com.softvision.ipm.pms.assign.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assign.constant.AssignmentPhaseStatus;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.model.CycleAssignmentDto;
import com.softvision.ipm.pms.assign.model.PhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.AssignmentPhaseDataRepository;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class ManagerAssignmentService {

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private AssignmentPhaseDataRepository assignmentPhaseDataRepository;

	@Autowired private RoleService roleService;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	public void changeManager(long phaseAssignId, int toEmployeeId)
			throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(phaseAssignId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		int status = employeePhaseAssignment.getStatus();
		AssignmentPhaseStatus assignmentPhaseStatus = AssignmentPhaseStatus.get(status);
		if (status > AssignmentPhaseStatus.SELF_APPRAISAL_PENDING.getCode()) {
			throw new ServiceException("Assignment is in '" + assignmentPhaseStatus.getName() + "' status. Cannot change the manager now");
		}
		if (!roleService.isManager(toEmployeeId)) {
			throw new ServiceException("The employee that you are trying to assign to, is not a manager");
		}
		// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changeManager(phaseAssignId, toEmployeeId);
		if (!changed) {
			throw new ServiceException("Unable to change assigned-by-manager");
		}
	}

	public void enableAppraisalFormToEmployee(long phaseAssignId) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(phaseAssignId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		int existingStatusId = employeePhaseAssignment.getStatus();
		AssignmentPhaseStatus existingStatus = AssignmentPhaseStatus.get(existingStatusId);
		//AssignmentPhaseStatus desiredStatus = AssignmentPhaseStatus.get(toStatusId);

		if (existingStatus != AssignmentPhaseStatus.ASSIGNED /*|| desiredStatus != AssignmentPhaseStatus.SELF_APPRAISAL_PENDING*/) {
			throw new ServiceException("Assignment status is not in ASSIGNED status.");
		}
		// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changeStatus(phaseAssignId, AssignmentPhaseStatus.SELF_APPRAISAL_PENDING.getCode());
		if (!changed) {
			throw new ServiceException("Unable to change assigned-by-manager");
		}
		// TODO email trigger
	}

	public List<CycleAssignmentDto> getAll(int employeeId) {
		List<CycleAssignmentDto> cycleAssignments = new ArrayList<>();
		List<AppraisalCycle> allCycles = appraisalCycleDataRepository.findAllByOrderByStartDateDesc();
		for (AppraisalCycle cycle : allCycles) {
			int cycleId = cycle.getId();
			System.out.println("cycleId=" + cycleId);
			CycleAssignmentDto cycleAssignment = new CycleAssignmentDto();
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
		System.out.println("cycleAssignments=" + cycleAssignments);
		return cycleAssignments;
	}

}
