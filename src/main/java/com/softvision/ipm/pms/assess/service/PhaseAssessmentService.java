package com.softvision.ipm.pms.assess.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assess.assembler.AssessmentAssembler;
import com.softvision.ipm.pms.assess.entity.AssessDetail;
import com.softvision.ipm.pms.assess.entity.AssessHeader;
import com.softvision.ipm.pms.assess.model.AssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.repo.AssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.util.AssignmentUtil;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(PhaseAssessmentService.class);

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private AssessmentHeaderDataRepository phaseAssessmentHeaderDataRepository;

	@Autowired private CycleAssessmentService cycleAssessmentService;

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private EmailRepository emailRepository;

	@PreSecureAssignment(permitEmployee=true, permitManager=true)
	public PhaseAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId) throws ServiceException {
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
		phaseAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeePhaseAssignment));

		int phaseId = employeePhaseAssignment.getPhaseId();
		phaseAssessment.setPhase(AppraisalAssembler.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

		long templateId = employeePhaseAssignment.getTemplateId();
		Template template = templateDataRepository.findById(templateId);
		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		phaseAssessment.setTemplateHeaders(TemplateAssembler.getTemplateHeaderDtoList(templateHeaders));

		List<AssessHeader> assessHeaders = phaseAssessmentHeaderDataRepository
				.findByAssignIdOrderByStatusAsc(assignmentId);

		int status = employeePhaseAssignment.getStatus();
		PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(status);
		// Restrict viewing the form by employee if the state is
		if (phaseAssignmentStatus == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
			// remove the top phase header if its requested by the manager
			if (requestedEmployeeId == employeePhaseAssignment.getAssignedBy()) {
				assessHeaders.remove(assessHeaders.size() - 1);
			}
		} else if (phaseAssignmentStatus == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
			// remove the top phase header if its requested by the employee
			if (requestedEmployeeId == employeePhaseAssignment.getEmployeeId()) {
				assessHeaders.remove(assessHeaders.size() - 1);
			}
		}
		phaseAssessment.setAssessHeaders(AssessmentAssembler.getAssessHeaderDtos(assessHeaders));
		return phaseAssessment;
	}

	@PreSecureAssignment(permitManager=true)
	public void enablePhaseAppraisal(long assignmentId, int fromEmployeeId) throws ServiceException {
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		PhaseAssignmentStatus nextStatus = AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), "enable",
				PhaseAssignmentStatus.NOT_INITIATED);
		int employeeId = employeePhaseAssignment.getEmployeeId();
		// check if any previous assignments are pending.
		EmployeeAssignmentDto incompletePhaseAssignment = managerAssignmentRepository
				.getPreviousIncompletePhaseAssignment(assignmentId, employeeId, employeePhaseAssignment.getPhaseId());
		if (incompletePhaseAssignment != null) {
			throw new ServiceException(
					"Make sure that assignments in previous phase are concluded for this employee before assigning new one");
		}
		employeePhaseAssignment.setStatus(nextStatus.getCode());
		phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// email trigger
		emailRepository.sendEnableMail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),
				employeeId);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void saveSelfAppraisal(long assignmentId, int requestedEmployeeId,
			AssessHeaderDto phaseAssessHeaderDto) throws ServiceException {
		update(phaseAssessHeaderDto, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED, "save",
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void submitSelfAppraisal(long assignmentId, int requestedEmployeeId,
			AssessHeaderDto phaseAssessHeaderDto) throws ServiceException {
		PhaseAssignment employeePhaseAssignment = update(phaseAssessHeaderDto,
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, "submit", PhaseAssignmentStatus.SELF_APPRAISAL_PENDING,
				PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
		// Email Trigger
		emailRepository.sendEmployeeSubmitMail(employeePhaseAssignment.getPhaseId(),
				employeePhaseAssignment.getEmployeeId(),phaseAssessHeaderDto.getAssessedBy());
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void saveReview(long assignmentId, int requestedEmployeeId, AssessHeaderDto phaseAssessHeaderDto)
			throws ServiceException {
		update(phaseAssessHeaderDto, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED, "save review",
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void submitReview(long assignmentId, int requestedEmployeeId, AssessHeaderDto phaseAssessHeaderDto)
			throws ServiceException {
		PhaseAssignment employeePhaseAssignment = update(phaseAssessHeaderDto,
				PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED, "submit", PhaseAssignmentStatus.MANAGER_REVIEW_PENDING,
				PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
		//  Email Trigger
		emailRepository.sendReviewCompleted(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),
				employeePhaseAssignment.getEmployeeId());
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void agree(long assignmentId, int requestedEmployeeId) throws ServiceException {
		PhaseAssignment employeePhaseAssignment = update(assignmentId, PhaseAssignmentStatus.EMPLOYEE_AGREED,
				"agree review", PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED,
				PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		// Email Trigger
		emailRepository.sendEmployeeAcceptence(employeePhaseAssignment.getPhaseId(),
				employeePhaseAssignment.getEmployeeId(), employeePhaseAssignment.getAssignedBy());
		// conclude
		conclude(assignmentId, requestedEmployeeId);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void escalate(long assignmentId, int requestedEmployeeId) throws ServiceException {
		PhaseAssignment employeePhaseAssignment = update(assignmentId, PhaseAssignmentStatus.EMPLOYEE_ESCALATED,
				"escalate", PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED);
		//  Email Trigger
		emailRepository.sendEmployeeRejected(employeePhaseAssignment.getPhaseId(),
				employeePhaseAssignment.getEmployeeId(), employeePhaseAssignment.getAssignedBy());
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void updateReview(long assignmentId, int requestedEmployeeId, AssessHeaderDto phaseAssessHeaderDto)
			throws ServiceException {
		PhaseAssignment employeePhaseAssignment = update(phaseAssessHeaderDto,
				PhaseAssignmentStatus.EMPLOYEE_ESCALATED, "update review", PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		//  Email trigger
		emailRepository.sendUpdatedReviewMail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),employeePhaseAssignment.getEmployeeId());
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	private void conclude(long assignmentId, int requestedEmployeeId) throws ServiceException {
		LOGGER.info("conclude(" + assignmentId + ", " + requestedEmployeeId + ")");
		// Change the status to CONCLUDED
		PhaseAssignment employeePhaseAssignment = update(assignmentId, PhaseAssignmentStatus.CONCLUDED,
				"conclude", PhaseAssignmentStatus.EMPLOYEE_AGREED);
		// Update score in employee assignment
		AssessHeader phaseAssessHeader = phaseAssessmentHeaderDataRepository
				.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		List<AssessDetail> assessDetails = phaseAssessHeader.getAssessDetails();
		double totalScore=0;
		for (AssessDetail assessDetail : assessDetails) {
			totalScore+=assessDetail.getScore();
		}
		employeePhaseAssignment.setScore(totalScore);
		phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// email trigger
		emailRepository.sendConcludeMail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),
				employeePhaseAssignment.getEmployeeId());
		// Abridge Cycle Assignment
		cycleAssessmentService.abridgeQuietly(employeePhaseAssignment.getEmployeeId());
	}

	private PhaseAssignment update(AssessHeaderDto phaseAssessHeaderDto,
			PhaseAssignmentStatus statusToUpdate, String message, PhaseAssignmentStatus... previousStatusesToCheck)
			throws ServiceException {
		long assignmentId = phaseAssessHeaderDto.getAssignId();
		LOGGER.info("update(" + assignmentId + ", operation=" + message + ", statusToUpdate=" + statusToUpdate + ")");
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), message, previousStatusesToCheck);
		phaseAssessHeaderDto.setAssessedBy(employeePhaseAssignment.getAssignedBy());
		ValidationUtil.validate(phaseAssessHeaderDto);
		// Change assignment status to statusToUpdate
		employeePhaseAssignment.setStatus(statusToUpdate.getCode());
		PhaseAssignment saved = phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// Update the assessment header status
		phaseAssessHeaderDto.setStatus(saved.getStatus());
		AssessHeader phaseAssessHeader = AssessmentAssembler.getAssessHeader(phaseAssessHeaderDto);
		phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		LOGGER.info("update(" + assignmentId + ", operation=" + message + ", statusToUpdate=" + statusToUpdate + ") SUCCESSFUL");
		return employeePhaseAssignment;
	}

	private PhaseAssignment update(long assignmentId, PhaseAssignmentStatus statusToUpdate, String message,
			PhaseAssignmentStatus... previousStatusesToCheck) throws ServiceException {
		LOGGER.info("update2(" + assignmentId + ", operation=" + message + ", statusToUpdate=" + statusToUpdate + ")");
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), message, previousStatusesToCheck);
		// Change assignment status to statusToUpdate
		employeePhaseAssignment.setStatus(statusToUpdate.getCode());
		PhaseAssignment saved = phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// Update the assessment header status
		AssessHeader phaseAssessHeader = phaseAssessmentHeaderDataRepository
				.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		phaseAssessHeader.setStatus(saved.getStatus());
		phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		LOGGER.info("update2(" + assignmentId + ", operation=" + message + ", statusToUpdate=" + statusToUpdate + ") SUCCESSFUL");
		return employeePhaseAssignment;
	}

}
