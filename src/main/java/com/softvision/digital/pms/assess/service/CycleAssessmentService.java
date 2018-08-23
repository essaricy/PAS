package com.softvision.digital.pms.assess.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.repo.AppraisalRepository;
import com.softvision.digital.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.assign.entity.CycleAssignment;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.digital.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.role.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CycleAssessmentService {

	@Autowired private RoleService roleService;

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	public void abridgeQuietly(int employeeId) {
		try {
			log.info("abridgeQuietly: START {} ", employeeId);
			abridge(employeeId);
			log.info("abridgeQuietly: END {} ", employeeId);
		} catch (Exception exception) {
			log.warn("abridgeQuietly: FAILED {} ERROR=", employeeId, "Abridge Failed due to the error=" + exception.getMessage());
		}
	}

	@Transactional
	public void abridge(int employeeId) throws ServiceException {
	    log.info("abridge: START {} ", employeeId);
		Optional<AppraisalCycle> optional = appraisalRepository.getActiveCycle();
		if (!optional.isPresent()) {
			throw new ServiceException("There is no active cycle");
		}
		AppraisalCycle activeCycle = optional.get();
		Integer cycleId = activeCycle.getId();
		List<AppraisalPhase> phases = activeCycle.getPhases();

		// If all phases are done then update the cycle assignment with score
		double cycleScore=0;
		int assignedBy=0;
		int numberOfPhases=phases.size();
		// Check if all the assignments are concluded.
		for (AppraisalPhase phase : phases) {
			Optional<PhaseAssignment> employeePhaseAssignmentOptional = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeIdAndStatus(phase.getId(), employeeId, PhaseAssignmentStatus.CONCLUDED.getCode());
			PhaseAssignment employeePhaseAssignment = employeePhaseAssignmentOptional.orElseThrow(() -> new ServiceException("There is a missing assignment for the phase " + phase.getName() + " for the employee " + employeeId));

			cycleScore+=employeePhaseAssignment.getScore();
			assignedBy=employeePhaseAssignment.getAssignedBy();
		}
		final int assignedByEmployeeId=assignedBy;
		Optional<CycleAssignment> employeeCycleAssignmentOptional = cycleAssignmentDataRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
		CycleAssignment employeeCycleAssignment = employeeCycleAssignmentOptional.orElseGet(() -> {
			// There is a missing cycle assignment. create one.
			CycleAssignment cycleAssignment = new CycleAssignment();
			cycleAssignment.setAssignedAt(new Date());
			cycleAssignment.setAssignedBy(assignedByEmployeeId);
			cycleAssignment.setCycleId(cycleId);
			cycleAssignment.setEmployeeId(employeeId);
			return cycleAssignment;
		});
		employeeCycleAssignment.setScore(cycleScore/numberOfPhases);
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.ABRIDGED.getCode());
		cycleAssignmentDataRepository.save(employeeCycleAssignment);
		log.info("abridge: END {} ", employeeId);
	}

	@Transactional
	public void assignCycleToNextLevelManager(long cycleAssignId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
		log.warn("assignCycleToNextLevelManager: START assignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
		Optional<CycleAssignment> cycleAssignmentOptional = cycleAssignmentDataRepository.findById(cycleAssignId);
		CycleAssignment employeeCycleAssignment = cycleAssignmentOptional.orElseThrow(() -> new ServiceException("Assignment does not exist."));

		// he must be the same employee who has been assigned
		int assignedBy = employeeCycleAssignment.getAssignedBy();
		if (assignedBy != fromEmployeeId) {
			throw new ServiceException("The manager who assessed the last phase can only assign it to the next level manager");
		}
		int status = employeeCycleAssignment.getStatus();
		CycleAssignmentStatus cycleAssignmentStatus = CycleAssignmentStatus.get(status);
		if (status != CycleAssignmentStatus.ABRIDGED.getCode()) {
			throw new ServiceException("Assignment is in '" + cycleAssignmentStatus.getName() + "' status. Cannot change the manager now");
		}
		if (!roleService.isManager(toEmployeeId)) {
			throw new ServiceException("The employee that you are trying to assign to, is not a manager");
		}
		employeeCycleAssignment.setAssignedBy(toEmployeeId);
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.CONCLUDED.getCode());
		cycleAssignmentDataRepository.save(employeeCycleAssignment);
		log.warn("assignCycleToNextLevelManager: END assignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
	}

}
