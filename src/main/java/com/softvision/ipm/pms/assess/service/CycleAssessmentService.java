package com.softvision.ipm.pms.assess.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class CycleAssessmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CycleAssessmentService.class);

	@Autowired private RoleService roleService;

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	public void abridgeQuietly(int employeeId) {
		try {
			LOGGER.info("abridgeQuietly: START {} ", employeeId);
			abridge(employeeId);
			LOGGER.info("abridgeQuietly: END {} ", employeeId);
		} catch (Exception exception) {
			LOGGER.warn("abridgeQuietly: FAILED {} ERROR=", employeeId, "Abridge Failed due to the error=" + exception.getMessage());
		}
	}

	@Transactional
	public void abridge(int employeeId) throws ServiceException {
	    LOGGER.info("abridge: START {} ", employeeId);
		AppraisalCycle activeCycle = appraisalRepository.getActiveCycle();
		Integer cycleId = activeCycle.getId();
		List<AppraisalPhase> phases = activeCycle.getPhases();

		// If all phases are done then update the cycle assignment with score
		double cycleScore=0;
		int assignedBy=0;
		long templateId=0;
		int numberOfPhases=phases.size();
		// Check if all the assignments are concluded.
		for (AppraisalPhase phase : phases) {
			PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeIdAndStatus(phase.getId(), employeeId, PhaseAssignmentStatus.CONCLUDED.getCode());
			if (employeePhaseAssignment == null) {
				throw new ServiceException("There is a missing assignment for the phase " + phase.getName() + " for the employee " + employeeId);
			}
			cycleScore+=employeePhaseAssignment.getScore();
			assignedBy=employeePhaseAssignment.getAssignedBy();
			templateId=employeePhaseAssignment.getTemplateId();
		}
		CycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
		if (employeeCycleAssignment == null) {
			// There is a missing cycle assignment. create one.
			employeeCycleAssignment = new CycleAssignment();
			employeeCycleAssignment.setAssignedAt(new Date());
			employeeCycleAssignment.setAssignedBy(assignedBy);
			employeeCycleAssignment.setCycleId(cycleId);
			employeeCycleAssignment.setEmployeeId(employeeId);
			employeeCycleAssignment.setTemplateId(templateId);
		}
		employeeCycleAssignment.setScore(cycleScore/numberOfPhases);
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.ABRIDGED.getCode());
		cycleAssignmentDataRepository.save(employeeCycleAssignment);
		LOGGER.info("abridge: END {} ", employeeId);
	}

	@Transactional
	public void assignCycleToNextLevelManager(long cycleAssignId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
		LOGGER.warn("assignCycleToNextLevelManager: START assignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
		CycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(cycleAssignId);
		if (employeeCycleAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
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
		LOGGER.warn("assignCycleToNextLevelManager: END assignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
	}

}
