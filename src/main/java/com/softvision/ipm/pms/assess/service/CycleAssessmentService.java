package com.softvision.ipm.pms.assess.service;

import java.util.Date;
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
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assess.model.CycleAssessmentDto;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.role.service.RoleService;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class CycleAssessmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CycleAssessmentService.class);

	@Autowired private RoleService roleService;

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	public void abridgeQuietly(int employeeId) {
		try {
			LOGGER.info("abridgeQuietly(" + employeeId + ")");
			abridge(employeeId);
			LOGGER.info("abridgeQuietly(" + employeeId + ") COMPLETED");
		} catch (Exception exception) {
			String error = "Abridge Failed due to the error=" + exception.getMessage();
			LOGGER.info("abridgeQuietly: FAILED. Error=" + error);
		}
	}

	@Transactional
	public void abridge(int employeeId) throws ServiceException {
		LOGGER.info("abridge(" + employeeId + ")");
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
			LOGGER.warn("employeeCycleAssignment does not exist. Creating a new one from the last phase assignment");
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
		LOGGER.info("abridge(" + employeeId + ") COMPLETED");
	}

	public CycleAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId) throws ServiceException {
		CycleAssessmentDto cycleAssessment = new CycleAssessmentDto();
		CycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(assignmentId);
		
		// Allow this form only to the employee and to the manager to whom its been assigned
		int assignedBy = employeeCycleAssignment.getAssignedBy();
		// TODO: Allow it to employee?
		//int employeeId = employeeCycleAssignment.getEmployeeId();
		if (requestedEmployeeId != assignedBy) {
			LOGGER.warn("UNAUTHORIZED ACCESS ATTEMPT: assignmentId: " + assignmentId + ", requestedEmployeeId:" + requestedEmployeeId);
			throw new ServiceException("No allowed to view appraisal form");
		}
		cycleAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeeCycleAssignment));

		int cycleId = employeeCycleAssignment.getCycleId();
		cycleAssessment.setCycle(AppraisalAssembler.getCycle(appraisalCycleDataRepository.findById(cycleId)));

		long templateId = employeeCycleAssignment.getTemplateId();
		Template template = templateDataRepository.findById(templateId);
		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		cycleAssessment.setTemplateHeaders(TemplateAssembler.getTemplateHeaderDtoList(templateHeaders));
		return cycleAssessment;
	}

	@Transactional
	public void assignCycleToNextLevelManager(long cycleAssignId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
		LOGGER.warn("assignCycleToNextLevelManager(" + cycleAssignId + ", " + fromEmployeeId + ", " + toEmployeeId + ")");
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
		LOGGER.warn("assignCycleToNextLevelManager(" + cycleAssignId + ", " + fromEmployeeId + ", " + toEmployeeId + ") Completed");
	}

}
