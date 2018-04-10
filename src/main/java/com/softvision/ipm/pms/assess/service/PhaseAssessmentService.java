package com.softvision.ipm.pms.assess.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assess.entity.AssessDetail;
import com.softvision.ipm.pms.assess.entity.AssessHeader;
import com.softvision.ipm.pms.assess.mapper.AssessMapper;
import com.softvision.ipm.pms.assess.model.AssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.repo.AssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.mapper.AssignmentMapper;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.util.AssignmentUtil;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.email.repo.EmailRepository;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.interceptor.annotations.PreSecureAssignment;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.mapper.TemplateMapper;
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

	@Autowired private EmployeeRepository employeeRepository;

	@Autowired private AppraisalMapper appraisalMapper;

	@Autowired private TemplateMapper templateMapper;

	@Autowired private AssignmentMapper assignmentMapper;

	@Autowired private AssessMapper assessMapper;

	@PreSecureAssignment(permitEmployee=true, permitManager=true)
	public PhaseAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId) throws ServiceException {
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
		EmployeeAssignmentDto employeeAssignment = assignmentMapper.get(employeePhaseAssignment);
		employeeAssignment.setAssignedTo(employeeRepository.findByEmployeeId(employeeAssignment.getAssignedTo().getEmployeeId()));
		phaseAssessment.setEmployeeAssignment(employeeAssignment);

		int phaseId = employeePhaseAssignment.getPhaseId();
		phaseAssessment.setPhase(appraisalMapper.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

		long templateId = employeePhaseAssignment.getTemplateId();
		Template template = templateDataRepository.findById(templateId);
		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		phaseAssessment.setTemplateHeaders(templateMapper.getTemplateHeaderDtoList(templateHeaders));

		List<AssessHeader> assessHeaders = phaseAssessmentHeaderDataRepository
				.findByAssignIdOrderByStatusAsc(assignmentId);

		int status = employeePhaseAssignment.getStatus();
		PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(status);
		if (phaseAssignmentStatus == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED
		        && requestedEmployeeId == employeePhaseAssignment.getAssignedBy()) {
		    // Restrict viewing the form by employee if the state is
		    // remove the top phase header if its requested by the manager
		    assessHeaders.remove(assessHeaders.size() - 1);
		} else if (phaseAssignmentStatus == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED
		        && requestedEmployeeId == employeePhaseAssignment.getEmployeeId()) {
		    // remove the top phase header if its requested by the employee
		    assessHeaders.remove(assessHeaders.size() - 1);
		}
		phaseAssessment.setAssessHeaders(assessMapper.getAssessHeaderList(assessHeaders));
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
	@PreSecureAssignment(permitManager=true)
	public void conclude(long assignmentId, int requestedEmployeeId) throws ServiceException {
		LOGGER.info("conclude: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);

		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		int employeeId = employeePhaseAssignment.getEmployeeId();
		int phaseId = employeePhaseAssignment.getPhaseId();
        AppraisalPhase nextPhase = appraisalPhaseDataRepository.findNextPhase(phaseId);
        // There must be goal set for next phase. Otherwise conclude is not allowed
        if (nextPhase == null) {
            LOGGER.error("conclude: There is no next phase set up for the phaseId={}", phaseId);
            throw new ServiceException("Cannot conclude this assignment as there is no appraisal phase set up for the next period. Please infrorm your admin to set up the next phase/cycle");
        } else {
            PhaseAssignment nextAssignment = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeId(nextPhase.getId(), employeeId);
            if (nextAssignment == null) {
                LOGGER.warn("Next phase assignment is not setup. cannot conclude. nextPhaseId={}, employeeId={}", nextPhase.getId(), employeeId);
                throw new ServiceException("Assignment is not set for the next phase. You can only conclude this when assignment is set for the next phase for this employee.");
            }
        }
		// Change the status to CONCLUDED
		employeePhaseAssignment = update(assignmentId, PhaseAssignmentStatus.CONCLUDED, "conclude", PhaseAssignmentStatus.EMPLOYEE_AGREED);
		// Update score in employee assignment
		AssessHeader phaseAssessHeader = phaseAssessmentHeaderDataRepository
				.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		List<AssessDetail> assessDetails = phaseAssessHeader.getAssessDetails();
		phaseAssignmentDataRepository.save(employeePhaseAssignment);
		employeePhaseAssignment.setScore(assessDetails.stream().mapToDouble(AssessDetail::getScore).sum());
		// email trigger
		emailRepository.sendConcludeMail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),
				employeePhaseAssignment.getEmployeeId());
		// Abridge Cycle Assignment
		cycleAssessmentService.abridgeQuietly(employeePhaseAssignment.getEmployeeId());
		LOGGER.info("conclude: END assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
	}

	private PhaseAssignment update(AssessHeaderDto phaseAssessHeaderDto,
			PhaseAssignmentStatus statusToUpdate, String message, PhaseAssignmentStatus... previousStatusesToCheck)
			throws ServiceException {
		long assignmentId = phaseAssessHeaderDto.getAssignId();
		LOGGER.info("update: START assignId={}, message={},statusToUpdate{}", assignmentId, message, statusToUpdate);
		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), message, previousStatusesToCheck);
		phaseAssessHeaderDto.setAssessedBy(employeePhaseAssignment.getAssignedBy());
		ValidationUtil.validate(phaseAssessHeaderDto);
		// Change assignment status to statusToUpdate
		employeePhaseAssignment.setStatus(statusToUpdate.getCode());
		PhaseAssignment saved = phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// Update the assessment header status
		phaseAssessHeaderDto.setStatus(saved.getStatus());
		AssessHeader phaseAssessHeader = assessMapper.getHeader(phaseAssessHeaderDto);
		phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		LOGGER.info("update: END assignId={}, message={},statusToUpdate{}", assignmentId, message, statusToUpdate);
		return employeePhaseAssignment;
	}

	private PhaseAssignment update(long assignmentId, PhaseAssignmentStatus statusToUpdate, String message,
			PhaseAssignmentStatus... previousStatusesToCheck) throws ServiceException {
	    LOGGER.info("update2: START assignId={}, message={},statusToUpdate{}", assignmentId, message, statusToUpdate);
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
		LOGGER.info("update2: END assignId={}, message={},statusToUpdate{}", assignmentId, message, statusToUpdate);
		return employeePhaseAssignment;
	}

}
