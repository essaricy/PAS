package com.softvision.digital.pms.assess.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.digital.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.digital.pms.assess.entity.AssessDetail;
import com.softvision.digital.pms.assess.entity.AssessHeader;
import com.softvision.digital.pms.assess.mapper.AssessMapper;
import com.softvision.digital.pms.assess.model.AssessDetailDto;
import com.softvision.digital.pms.assess.model.AssessHeaderDto;
import com.softvision.digital.pms.assess.model.PhaseAssessmentDto;
import com.softvision.digital.pms.assess.repo.AssessmentHeaderDataRepository;
import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.assign.entity.CycleAssignment;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.mapper.AssignmentMapper;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.digital.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.digital.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.digital.pms.assign.util.AssignmentUtil;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.util.ValidationUtil;
import com.softvision.digital.pms.email.repo.EmailRepository;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.mapper.EmployeeMapper;
import com.softvision.digital.pms.employee.repo.EmployeeDataRepository;
import com.softvision.digital.pms.interceptor.annotations.InjectAssignment;
import com.softvision.digital.pms.interceptor.annotations.PreSecureAssignment;
import com.softvision.digital.pms.template.entity.Template;
import com.softvision.digital.pms.template.entity.TemplateHeader;
import com.softvision.digital.pms.template.mapper.TemplateMapper;
import com.softvision.digital.pms.template.repo.TemplateDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhaseAssessmentService {

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private AssessmentHeaderDataRepository assessmentHeaderDataRepository;

	@Autowired private CycleAssessmentService cycleAssessmentService;

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private EmailRepository emailRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired private AppraisalMapper appraisalMapper;

	@Autowired private TemplateMapper templateMapper;

	@Autowired private AssignmentMapper assignmentMapper;

	@Autowired private AssessMapper assessMapper;

	@Autowired EmployeeMapper employeeMapper;

	@PreSecureAssignment(permitEmployee=true, permitManager=true)
	public PhaseAssessmentDto getByAssignmentForEmployeeAndManager(long assignmentId, int requestedEmployeeId,
			@InjectAssignment PhaseAssignment phaseAssignment) {
		return getByAssignment(assignmentId, requestedEmployeeId, phaseAssignment);
    }

	public PhaseAssessmentDto getByAssignmentForSubmittedToManager(int cycleId, int employeeId, long assignId,
			int requestedEmployeeId) {
		log.info("getByAssignmentForSubmittedToManager: START cycleId={}, employeeId={}, assignId={}, requestedEmployeeId={}",
				cycleId, employeeId, assignId, requestedEmployeeId);

		CycleAssignment cycleAssignment = cycleAssignmentDataRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
		if (cycleAssignment == null) {
			throw new ValidationException("Invalid cycle assignment");
		}
		// Check if the right Manager is calling
		Integer submittedTo = cycleAssignment.getSubmittedTo();
		if (submittedTo == null || submittedTo != requestedEmployeeId) {
            throw new ValidationException("getByAssignmentForSubmittedToManager: UNAUTHORIZED ACCESS ATTEMPTED");
        }
		PhaseAssignment phaseAssignment = phaseAssignmentDataRepository.findById(assignId);
		return getByAssignment(assignId, requestedEmployeeId, phaseAssignment);
    }

	private PhaseAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId,
			@InjectAssignment PhaseAssignment phaseAssignment) {
        PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
        EmployeeAssignmentDto employeeAssignment = assignmentMapper.get(phaseAssignment);

        Employee assignedToEmployee = employeeRepository.findByEmployeeId(employeeAssignment.getAssignedTo().getEmployeeId());
        employeeAssignment.setAssignedTo(employeeMapper.getEmployeeDto(assignedToEmployee));

        Employee assignedByEmployee = employeeRepository.findByEmployeeId(employeeAssignment.getAssignedBy().getEmployeeId());
        employeeAssignment.setAssignedBy(employeeMapper.getEmployeeDto(assignedByEmployee));
        phaseAssessment.setEmployeeAssignment(employeeAssignment);

        int phaseId = phaseAssignment.getPhaseId();
        phaseAssessment.setPhase(appraisalMapper.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

        long templateId = phaseAssignment.getTemplateId();
        Template template = templateDataRepository.findById(templateId);
        List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
        phaseAssessment.setTemplateHeaders(templateMapper.getTemplateHeaderDtoList(templateHeaders));

        List<AssessHeader> assessHeaders = assessmentHeaderDataRepository.findByAssignIdOrderByStageAsc(assignmentId);

        removeWIP(requestedEmployeeId, phaseAssignment, assessHeaders);
        phaseAssessment.setAssessHeaders(assessMapper.getAssessHeaderList(assessHeaders));
        return phaseAssessment;
    }

	private void removeWIP(int requestedEmployeeId, PhaseAssignment phaseAssignment,
			List<AssessHeader> assessHeaders) {
		boolean isEmployee = requestedEmployeeId == phaseAssignment.getEmployeeId();
		boolean isManager = requestedEmployeeId == phaseAssignment.getAssignedBy();

		int status = phaseAssignment.getStatus();
        PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(status);
        if (isManager && phaseAssignmentStatus == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
            // Show nothing to managers if self appraisal is in progress
            assessHeaders.clear();
        } else if (isEmployee && phaseAssignmentStatus == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
            // remove the top phase header if its requested by the employee
            assessHeaders.removeIf(assessHeader -> assessHeader.getStage() != 0);
        }
	}

	@PreSecureAssignment(permitManager=true)
	public void enablePhaseAppraisal(long assignmentId, int fromEmployeeId,
			@InjectAssignment PhaseAssignment phaseAssignment) throws ServiceException {
		PhaseAssignmentStatus nextStatus = AssignmentUtil.validateStatus(phaseAssignment.getStatus(), "enable",
				PhaseAssignmentStatus.NOT_INITIATED);
		int employeeId = phaseAssignment.getEmployeeId();
		// check if any previous assignments are pending.
		EmployeeAssignmentDto incompletePhaseAssignment = managerAssignmentRepository
				.getPreviousIncompletePhaseAssignment(assignmentId, employeeId, phaseAssignment.getPhaseId());
		if (incompletePhaseAssignment != null) {
			throw new ServiceException(
					"Make sure that assignments in previous phase are concluded for this employee before assigning new one");
		}
		phaseAssignment.setStatus(nextStatus.getCode());
		phaseAssignmentDataRepository.save(phaseAssignment);
		// email trigger
		emailRepository.sendEnableEmployeeAppraisalEmail(phaseAssignment.getPhaseId(), phaseAssignment.getAssignedBy(),
				employeeId);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void saveSelfAppraisal(long assignmentId, int requestedEmployeeId,
			AssessHeaderDto assessHeaderDto, @InjectAssignment PhaseAssignment phaseAssignment) throws ServiceException {
		log.info("saveSelfAppraisal: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssessment(assignmentId, phaseAssignment, assessHeaderDto, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED, "save",
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED, PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED);
		log.info("saveSelfAppraisal: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void submitSelfAppraisal(long assignmentId, int requestedEmployeeId, AssessHeaderDto assessHeaderDto,
			@InjectAssignment PhaseAssignment phaseAssignment) throws ServiceException {
		log.info("submitSelfAppraisal: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssessment(assignmentId, phaseAssignment, assessHeaderDto, PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, "submit",
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED,
				PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED);

		// Email Trigger
		emailRepository.sendEmployeeSubmittedEmail(phaseAssignment.getPhaseId(),
				phaseAssignment.getEmployeeId(), phaseAssignment.getAssignedBy());
		log.info("submitSelfAppraisal: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitManager = true)
	public void revertToSelfSubmission(long assignmentId, int requestedEmployeeId,
			@InjectAssignment PhaseAssignment phaseAssignment) throws ServiceException {
		log.info("revertToSelfSubmission: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssignment(assignmentId, phaseAssignment, PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED,
				"self appraisal reverted", PhaseAssignmentStatus.MANAGER_REVIEW_PENDING);

		// Email Trigger
		emailRepository.sendRevertToSelfSubmissionEmail(phaseAssignment.getPhaseId(),
				phaseAssignment.getAssignedBy(), phaseAssignment.getEmployeeId());
		log.info("revertToSelfSubmission: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void saveReview(long assignmentId, int requestedEmployeeId, AssessHeaderDto assessHeaderDto,
			@InjectAssignment PhaseAssignment phaseAssignment)
			throws ServiceException {
		log.info("saveReview: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssessment(assignmentId, phaseAssignment, assessHeaderDto, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED, "save review",
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
		log.info("saveReview: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void submitReview(long assignmentId, int requestedEmployeeId, AssessHeaderDto assessHeaderDto,
			@InjectAssignment PhaseAssignment phaseAssignment) throws ServiceException {
		log.info("saveReview: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssessment(assignmentId, phaseAssignment, assessHeaderDto, PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED, "submit review",
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
		//  Email Trigger
		emailRepository.sendManagerReviewCompletedEmail(phaseAssignment.getPhaseId(), phaseAssignment.getAssignedBy(),
				phaseAssignment.getEmployeeId());
		log.info("saveReview: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void agree(long assignmentId, int requestedEmployeeId, @InjectAssignment PhaseAssignment phaseAssignment)
			throws ServiceException {
		log.info("agree: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssignment(assignmentId, phaseAssignment, PhaseAssignmentStatus.EMPLOYEE_AGREED, "agree review",
				PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED, PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		// Email Trigger
		emailRepository.sendEmployeeAgreedWithReviewEmail(phaseAssignment.getPhaseId(),
				phaseAssignment.getEmployeeId(), phaseAssignment.getAssignedBy());
		log.info("agree: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);

	}

	@Transactional
	@PreSecureAssignment(permitEmployee = true)
	public void disagree(long assignmentId, int requestedEmployeeId, @InjectAssignment PhaseAssignment phaseAssignment)
			throws ServiceException {
		log.info("disagree: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssignment(assignmentId, phaseAssignment, PhaseAssignmentStatus.EMPLOYEE_ESCALATED, "disagree review",
				PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED);
		// Email Trigger
		emailRepository.sendEmployeeDisagreedWithReviewEmail(phaseAssignment.getPhaseId(),
				phaseAssignment.getEmployeeId(), phaseAssignment.getAssignedBy());
		log.info("disagree: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void updateReview(long assignmentId, int requestedEmployeeId, AssessHeaderDto assessHeaderDto,
			@InjectAssignment PhaseAssignment phaseAssignment) throws ServiceException {
		log.info("updateReview: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
		updateAssessment(assignmentId, phaseAssignment, assessHeaderDto, PhaseAssignmentStatus.EMPLOYEE_ESCALATED,
				"update review", PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		//  Email trigger
		emailRepository.sendUpdatedReviewEmail(phaseAssignment.getPhaseId(),
				phaseAssignment.getAssignedBy(), phaseAssignment.getEmployeeId());
		log.info("updateReview: END assignId={}, requestedEmployeeId={}, phaseAssignment={}", assignmentId,
				requestedEmployeeId, phaseAssignment);
	}

	@Transactional
	@PreSecureAssignment(permitManager = true)
	public void conclude(long assignmentId, int requestedEmployeeId, @InjectAssignment PhaseAssignment phaseAssignment)
			throws ServiceException {
		log.info("conclude: START assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);

		int employeeId = phaseAssignment.getEmployeeId();
		int phaseId = phaseAssignment.getPhaseId();
        AppraisalPhase nextPhase = appraisalPhaseDataRepository.findNextPhase(phaseId);
        // There must be goal set for next phase. Otherwise conclude is not allowed
        if (nextPhase == null) {
            log.error("conclude: There is no next phase set up for the phaseId={}", phaseId);
            throw new ServiceException("Cannot conclude this assignment as there is no appraisal phase set up for the next period. Please infrorm your admin to set up the next phase/cycle");
        } else {
            PhaseAssignment nextAssignment = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeId(nextPhase.getId(), employeeId);
            if (nextAssignment == null) {
                log.warn("Next phase assignment is not setup. cannot conclude. nextPhaseId={}, employeeId={}", nextPhase.getId(), employeeId);
                throw new ServiceException("Assignment is not set for the next phase. You can only conclude this when assignment is set for the next phase for this employee.");
            }
        }
		// Change the status to CONCLUDED
		updateAssignment(assignmentId, phaseAssignment, PhaseAssignmentStatus.CONCLUDED, "conclude", PhaseAssignmentStatus.EMPLOYEE_AGREED);

		// Update score in employee assignment
		AssessHeader assessHeader = assessmentHeaderDataRepository.findFirstByAssignIdAndStage(assignmentId,
				PhaseAssignmentStatus.CONCLUDED.getStage());
		List<AssessDetail> assessDetails = assessHeader.getAssessDetails();

		phaseAssignment.setScore(assessDetails.stream().mapToDouble(AssessDetail::getScore).sum());
		phaseAssignmentDataRepository.save(phaseAssignment);

		// email trigger
		emailRepository.sendConcludeMail(phaseAssignment.getPhaseId(), phaseAssignment.getAssignedBy(), phaseAssignment.getEmployeeId());

		// Abridge Cycle Assignment
		cycleAssessmentService.abridgeQuietly(phaseAssignment.getEmployeeId());
		log.info("conclude: END assignId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
	}

	private void updateAssessment(long assignmentId, PhaseAssignment phaseAssignment, AssessHeaderDto assessHeaderDto,
			PhaseAssignmentStatus statusToUpdate, String message, PhaseAssignmentStatus... previousStatusesToCheck)
			throws ServiceException {
		AssignmentUtil.validateStatus(phaseAssignment.getStatus(), message, previousStatusesToCheck);

		PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(phaseAssignment.getStatus());
		if (phaseAssignmentStatus == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
				|| phaseAssignmentStatus == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
			assessHeaderDto.setId(0);
			assessHeaderDto.getAssessDetails().stream().forEach(assessDetail -> assessDetail.setId(0));
		} else {
			AssessHeader assessHeader = assessmentHeaderDataRepository.findFirstByAssignIdAndStage(assignmentId,
					phaseAssignmentStatus.getStage());

			assessHeaderDto.setId(assessHeader.getId());

			List<AssessDetailDto> filteredDetails = assessHeaderDto.getAssessDetails().stream().filter(assessDetailDto -> {
				Optional<AssessDetail> optional = assessHeader.getAssessDetails().stream().filter(asessDetail -> asessDetail.getTemplateHeaderId() == assessDetailDto.getTemplateHeaderId()).findFirst();
				if (optional.isPresent()) {
					assessDetailDto.setId(optional.get().getId());
				}
				return optional.isPresent();
			}).collect(Collectors.toList());
			assessHeaderDto.setAssessDetails(filteredDetails);
		}
		assessHeaderDto.setStage(statusToUpdate.getStage());
		assessHeaderDto.setAssessDate(new Date());
		assessHeaderDto.setAssessedBy(phaseAssignment.getAssignedBy());
		assessHeaderDto.setStatus(statusToUpdate.getCode());
		assessHeaderDto.setAssignId(assignmentId);
		ValidationUtil.validate(assessHeaderDto);

		// Save phase assign
		phaseAssignment.setStatus(assessHeaderDto.getStatus());
		phaseAssignmentDataRepository.save(phaseAssignment);

		// Save phase assess header and detail
		AssessHeader assessHeader = assessMapper.getHeader(assessHeaderDto);
		assessmentHeaderDataRepository.save(assessHeader);
	}

	private void updateAssignment(long assignmentId, PhaseAssignment phaseAssignment, PhaseAssignmentStatus statusToUpdate,
			String message, PhaseAssignmentStatus... previousStatusesToCheck) throws ServiceException {

		AssignmentUtil.validateStatus(phaseAssignment.getStatus(), message, previousStatusesToCheck);
		// Change assignment status to statusToUpdate
		phaseAssignment.setStatus(statusToUpdate.getCode());
		phaseAssignmentDataRepository.save(phaseAssignment);
		// Update the assessment header status
		PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(phaseAssignment.getStatus());
		AssessHeader assessHeader = assessmentHeaderDataRepository.findFirstByAssignIdAndStage(assignmentId,
				phaseAssignmentStatus.getStage());
		assessHeader.setStatus(statusToUpdate.getCode());
		assessHeader.setStage(statusToUpdate.getStage());
		assessHeader.setAssessDate(new Date());
		assessmentHeaderDataRepository.save(assessHeader);
	}

	public PhaseAssessmentDto getAssignmentForAudit(long assignmentId) throws ServiceException {
		PhaseAssignment phaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (phaseAssignment == null) {
			throw new ServiceException("No such assignment exists");
		}

        PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
        EmployeeAssignmentDto employeeAssignment = assignmentMapper.get(phaseAssignment);

        Employee assignedToEmployee = employeeRepository.findByEmployeeId(employeeAssignment.getAssignedTo().getEmployeeId());
        employeeAssignment.setAssignedTo(employeeMapper.getEmployeeDto(assignedToEmployee));

        Employee assignedByEmployee = employeeRepository.findByEmployeeId(employeeAssignment.getAssignedBy().getEmployeeId());
        employeeAssignment.setAssignedBy(employeeMapper.getEmployeeDto(assignedByEmployee));
        phaseAssessment.setEmployeeAssignment(employeeAssignment);

        int phaseId = phaseAssignment.getPhaseId();
        phaseAssessment.setPhase(appraisalMapper.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

        long templateId = phaseAssignment.getTemplateId();
        Template template = templateDataRepository.findById(templateId);
        List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
        phaseAssessment.setTemplateHeaders(templateMapper.getTemplateHeaderDtoList(templateHeaders));

        List<AssessHeader> assessHeaders = assessmentHeaderDataRepository.findByAssignIdOrderByStageAsc(assignmentId);

        phaseAssessment.setAssessHeaders(assessMapper.getAssessHeaderList(assessHeaders));
        return phaseAssessment;
    }

}
