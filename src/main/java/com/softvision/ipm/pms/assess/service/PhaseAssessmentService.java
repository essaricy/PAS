package com.softvision.ipm.pms.assess.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assess.assembler.PhaseAssessmentAssembler;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.repo.PhaseAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.email.repo.EmailRepository;
import com.softvision.ipm.pms.interceptor.annotations.PreSecureAssignment;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class PhaseAssessmentService {

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private PhaseAssessmentHeaderDataRepository phaseAssessmentHeaderDataRepository;

	@Autowired private CycleAssessmentService cycleAssessmentService;

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private EmailRepository emailRepository;

	@PreSecureAssignment(permitEmployee=true, permitManager=true)
	public PhaseAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId) throws ServiceException {
		System.out.println("################### getByAssignment()");
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
		phaseAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeePhaseAssignment));

		int phaseId = employeePhaseAssignment.getPhaseId();
		phaseAssessment.setPhase(AppraisalAssembler.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

		long templateId = employeePhaseAssignment.getTemplateId();
		Template template = templateDataRepository.findById(templateId);
		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		phaseAssessment.setTemplateHeaders(TemplateAssembler.getTemplateHeaderDtoList(templateHeaders));

		List<PhaseAssessHeader> phaseAssessHeaders = phaseAssessmentHeaderDataRepository.findByAssignIdOrderByStatusAsc(assignmentId);
		phaseAssessment.setPhaseAssessmentHeaders(PhaseAssessmentAssembler.getPhaseAssessHeaderDtos(phaseAssessHeaders));
		return phaseAssessment;
	}

	@Transactional
	public void save(int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessmentHeaderDto)
			throws ServiceException {
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int employeeId = employeePhaseAssignment.getEmployeeId();
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		if (employeeId != requestedEmployeeId) {
			throw new ServiceException("Assignment can only be saved by the employee to whom its been assigned to");
		}
		phaseAssessmentHeaderDto.setAssessedBy(assignedBy);
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		if (latestHeader != null) {
			PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
			if (latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
					&& latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
				throw new ServiceException("The assessment form is not in a state to modify now.");
			}
		}
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = PhaseAssessmentAssembler.getPhaseAssessHeader(phaseAssessmentHeaderDto);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println(saved);
		// Change assignment status to 20
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.getCode());
		// Move assignment to next status
		phaseAssignmentDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void submit(int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int employeeId = employeePhaseAssignment.getEmployeeId();
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		if (employeeId != requestedEmployeeId) {
			throw new ServiceException("Assignment can only be submitted by the employee to whom its been assigned to");
		}
		phaseAssessmentHeaderDto.setAssessedBy(assignedBy);
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		if (latestHeader != null) {
			PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
			if (latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
					&& latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
				throw new ServiceException("The assessment form is not in a state to submit now.");
			}
		}
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = PhaseAssessmentAssembler.getPhaseAssessHeader(phaseAssessmentHeaderDto);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println(saved);
		// Change assignment status to 30
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.getCode());
		// Move assignment to next status
		phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// Email Trigger
	
		emailRepository.sendEmployeeSubmitMail(employeePhaseAssignment.getPhaseId(),
				phaseAssessmentHeaderDto.getAssessedBy(), employeePhaseAssignment.getEmployeeId());
	}

	@Transactional
	public void review(int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		if (assignedBy != requestedEmployeeId) {
			throw new ServiceException("Assignment can only be reviewed by the manager to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		if (latestHeader == null) {
			throw new ServiceException("The assessment form is not in a state to review now.");
		}
		PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
		if (latestStatus != PhaseAssignmentStatus.MANAGER_REVIEW_PENDING
				&& latestStatus != PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
			throw new ServiceException("Review can only be done when the employee appraisal form is submitted.");
		}
		phaseAssessmentHeaderDto.setAssessedBy(assignedBy);
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = PhaseAssessmentAssembler.getPhaseAssessHeader(phaseAssessmentHeaderDto);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println(saved);
		// Change assignment status to 40
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		// Move assignment to next status
		phaseAssignmentDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void reviewAndConclude(int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		//int assessedBy = phaseAssessmentHeaderDto.getAssessedBy();

		validateBeforeConclude(assignmentId, requestedEmployeeId);
		// Save the form first
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		phaseAssessmentHeaderDto.setAssessedBy(employeePhaseAssignment.getAssignedBy());
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = PhaseAssessmentAssembler.getPhaseAssessHeader(phaseAssessmentHeaderDto);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println(saved);
		// CONCLUDE the assignment
		conclude(assignmentId, requestedEmployeeId);
	}

	@Transactional
	public void conclude(long assignmentId, int requestedEmployeeId) throws ServiceException {
		validateBeforeConclude(assignmentId, requestedEmployeeId);

		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);

		// Change assignment status to CONCLUDED
		boolean changed = managerAssignmentRepository.changeStatus(assignmentId, PhaseAssignmentStatus.CONCLUDED.getCode());
		if (!changed) {
			throw new ServiceException("Unable to CONCLUDE this assignment");
		}
		employeePhaseAssignment = phaseAssignmentDataRepository
				.findByPhaseIdAndEmployeeIdAndStatus(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getEmployeeId(), PhaseAssignmentStatus.CONCLUDED.getCode());

		// Change last assessment status to CONCLUDED
		latestHeader.setStatus(PhaseAssignmentStatus.CONCLUDED.getCode());
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(latestHeader);
		System.out.println("######################## assessment status changed with status " + saved.getStatus());

		// Abridge Cycle Assignment 
		cycleAssessmentService.abridge(employeePhaseAssignment.getEmployeeId());
		// email trigger
		emailRepository.sendConcludeMail(employeePhaseAssignment.getPhaseId(),requestedEmployeeId,employeePhaseAssignment.getEmployeeId());
	}

	private void validateBeforeConclude(long assignmentId, int requestedEmployeeId) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		if (requestedEmployeeId != assignedBy) {
			throw new ServiceException("Assignment can only be concluded by the manager to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		if (latestHeader == null) {
			throw new ServiceException("The assessment form is not in a state to CONCLUDE now.");
		}
		PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
		if (latestStatus != PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
			throw new ServiceException("CONCLUDE can only be done when the manager review is completed.");
		}
	}

}
