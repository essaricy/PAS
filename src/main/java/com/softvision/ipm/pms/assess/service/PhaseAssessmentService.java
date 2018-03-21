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

		System.out.println("employeePhaseAssignment=" + employeePhaseAssignment);

		List<PhaseAssessHeader> phaseAssessHeaders = phaseAssessmentHeaderDataRepository
				.findByAssignIdOrderByStatusAsc(assignmentId);
		System.out.println("phaseAssessHeaders=" + phaseAssessHeaders);

		int status = employeePhaseAssignment.getStatus();
		PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(status);
		System.out.println("phaseAssignmentStatus= " + phaseAssignmentStatus);
		System.out.println("requestedEmployeeId= " + requestedEmployeeId);
		// Restrict viewing the form by employee if the state is
		if (phaseAssignmentStatus == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
			// remove the top phase header if its requested by the manager
			if (requestedEmployeeId == employeePhaseAssignment.getAssignedBy()) {
				phaseAssessHeaders.remove(phaseAssessHeaders.size() - 1);
			}
		} else if (phaseAssignmentStatus == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
			// remove the top phase header if its requested by the employee
			if (requestedEmployeeId == employeePhaseAssignment.getEmployeeId()) {
				phaseAssessHeaders.remove(phaseAssessHeaders.size() - 1);
			}
		}
		phaseAssessment.setPhaseAssessHeaders(PhaseAssessmentAssembler.getPhaseAssessHeaderDtos(phaseAssessHeaders));
		System.out.println("phaseAssessment=" + phaseAssessment);
		return phaseAssessment;
	}

	@PreSecureAssignment(permitManager=true)
	public void enablePhaseAppraisal(long assignmentId, int fromEmployeeId) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
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
		EmployeePhaseAssignment saved = phaseAssignmentDataRepository.save(employeePhaseAssignment);
		System.out.println(saved);
		// email trigger
		emailRepository.sendEnableMail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getAssignedBy(),
				employeeId);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void saveSelfAppraisal(long assignmentId, int requestedEmployeeId,
			PhaseAssessHeaderDto phaseAssessHeaderDto) throws ServiceException {
		update(phaseAssessHeaderDto, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED, "save",
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void submitSelfAppraisal(long assignmentId, int requestedEmployeeId,
			PhaseAssessHeaderDto phaseAssessHeaderDto) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = update(phaseAssessHeaderDto,
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, "submit", PhaseAssignmentStatus.SELF_APPRAISAL_PENDING,
				PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
		// Email Trigger
		emailRepository.sendEmployeeSubmitMail(employeePhaseAssignment.getPhaseId(),
				phaseAssessHeaderDto.getAssessedBy(), employeePhaseAssignment.getEmployeeId());
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void saveReview(long assignmentId, int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessHeaderDto)
			throws ServiceException {
		update(phaseAssessHeaderDto, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED, "save review",
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
	}

	@Transactional
	@PreSecureAssignment(permitManager=true)
	public void submitReview(long assignmentId, int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessHeaderDto)
			throws ServiceException {
		update(phaseAssessHeaderDto, PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED, "submit",
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
		// TODO Email Trigger
		/*
		 * emailRepository.sendManagerSubmitMail(employeePhaseAssignment.getPhaseId(),
		 * phaseAssessHeaderDto.getAssessedBy(),
		 * employeePhaseAssignment.getEmployeeId());
		 */
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void agree(long assignmentId, int requestedEmployeeId) throws ServiceException {
		update(assignmentId, PhaseAssignmentStatus.EMPLOYEE_AGREED, "agree review",
				PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED, PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		// TODO send email for agree
		// Email Trigger
		/*
		 * emailRepository.sendManagerSubmitMail(employeePhaseAssignment.getPhaseId(),
		 * phaseAssessHeaderDto.getAssessedBy(),
		 * employeePhaseAssignment.getEmployeeId());
		 */
		// TODO conclude
		conclude(assignmentId, requestedEmployeeId);
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void escalate(long assignmentId, int requestedEmployeeId) throws ServiceException {
		update(assignmentId, PhaseAssignmentStatus.EMPLOYEE_ESCALATED, "escalate",
				PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED);
		// TODO conclude
		// TODO Email Trigger
		/*
		 * emailRepository.sendManagerSubmitMail(employeePhaseAssignment.getPhaseId(),
		 * phaseAssessHeaderDto.getAssessedBy(),
		 * employeePhaseAssignment.getEmployeeId());
		 */
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	public void updateReview(long assignmentId, int requestedEmployeeId, PhaseAssessHeaderDto phaseAssessHeaderDto)
			throws ServiceException {
		update(phaseAssessHeaderDto, PhaseAssignmentStatus.EMPLOYEE_ESCALATED, "update review",
				PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		// TODO Email trigger
	}

	@Transactional
	@PreSecureAssignment(permitEmployee=true)
	private void conclude(long assignmentId, int requestedEmployeeId) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = update(assignmentId, PhaseAssignmentStatus.CONCLUDED,
				"conclude", PhaseAssignmentStatus.EMPLOYEE_AGREED);
		// Abridge Cycle Assignment
		cycleAssessmentService.abridge(employeePhaseAssignment.getEmployeeId());
		// email trigger
		emailRepository.sendConcludeMail(employeePhaseAssignment.getPhaseId(), requestedEmployeeId,
				employeePhaseAssignment.getEmployeeId());
	}

	private EmployeePhaseAssignment update(PhaseAssessHeaderDto phaseAssessHeaderDto,
			PhaseAssignmentStatus statusToUpdate, String message, PhaseAssignmentStatus... previousStatusesToCheck)
			throws ServiceException {
		long assignmentId = phaseAssessHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), message, previousStatusesToCheck);
		phaseAssessHeaderDto.setAssessedBy(employeePhaseAssignment.getAssignedBy());
		ValidationUtil.validate(phaseAssessHeaderDto);
		// Change assignment status to statusToUpdate
		employeePhaseAssignment.setStatus(statusToUpdate.getCode());
		EmployeePhaseAssignment saved = phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// Update the assessment header status
		phaseAssessHeaderDto.setStatus(saved.getStatus());
		PhaseAssessHeader phaseAssessHeader = PhaseAssessmentAssembler.getPhaseAssessHeader(phaseAssessHeaderDto);
		phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		return employeePhaseAssignment;
	}

	private EmployeePhaseAssignment update(long assignmentId, PhaseAssignmentStatus statusToUpdate, String message,
			PhaseAssignmentStatus... previousStatusesToCheck) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), message, previousStatusesToCheck);
		// Change assignment status to statusToUpdate
		employeePhaseAssignment.setStatus(statusToUpdate.getCode());
		EmployeePhaseAssignment saved = phaseAssignmentDataRepository.save(employeePhaseAssignment);
		// Update the assessment header status
		PhaseAssessHeader phaseAssessHeader = phaseAssessmentHeaderDataRepository
				.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		phaseAssessHeader.setStatus(saved.getStatus());
		phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		return employeePhaseAssignment;
	}

}
