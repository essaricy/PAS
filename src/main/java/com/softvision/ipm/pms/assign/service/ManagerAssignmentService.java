package com.softvision.ipm.pms.assign.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.ipm.pms.assign.model.PhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.util.AssignmentUtil;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.email.repo.EmailRepository;
import com.softvision.ipm.pms.interceptor.annotations.PreSecureAssignment;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class ManagerAssignmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManagerAssignmentService.class);

	@Autowired private RoleService roleService;

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;
	
	@Autowired private EmailRepository emailRepository;

	@PreSecureAssignment(permitManager=true)
	public void assignToAnotherManager(long assignmentId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
		LOGGER.warn("assignToAnotherManager(" + assignmentId + ", " + fromEmployeeId + ", " + toEmployeeId + ")");
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), "change manager",
				PhaseAssignmentStatus.NOT_INITIATED, PhaseAssignmentStatus.SELF_APPRAISAL_PENDING);
		if (!roleService.isManager(toEmployeeId)) {
			LOGGER.warn("assignToAnotherManager(" + assignmentId + ", " + fromEmployeeId + ", " + toEmployeeId + "): The employee that you are trying to assign to, is not a manager");
			throw new ServiceException("The employee that you are trying to assign to, is not a manager");
		}
		employeePhaseAssignment.setAssignedBy(toEmployeeId);
		phaseAssignmentDataRepository.save(employeePhaseAssignment);

		// email trigger
		emailRepository.sendChangeManager(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getEmployeeId(),
				fromEmployeeId, toEmployeeId);
	}

	public void sendRemiderToSubmit(long phaseAssignId, int loggedInEmployeeId) {
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(phaseAssignId);
		// email trigger
		emailRepository.sendEnableMail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),
				employeePhaseAssignment.getEmployeeId());
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

	@Transactional
	public void submitCycle(long cycleAssignId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
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
		employeeCycleAssignment.setSubmittedTo(toEmployeeId);
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.CONCLUDED.getCode());
		cycleAssignmentDataRepository.save(employeeCycleAssignment);
	}

}
