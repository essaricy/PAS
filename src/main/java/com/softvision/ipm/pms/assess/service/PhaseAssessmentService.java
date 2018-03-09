package com.softvision.ipm.pms.assess.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assess.assembler.AssessmentAssembler;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.repo.PhaseAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.AssignmentPhaseDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class PhaseAssessmentService {

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private AssignmentPhaseDataRepository assignmentPhaseDataRepository;

	@Autowired private PhaseAssessmentHeaderDataRepository phaseAssessmentHeaderDataRepository;

	public PhaseAssessmentDto getByAssignment(long assignmentId) {
		PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
		phaseAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeePhaseAssignment));

		int phaseId = employeePhaseAssignment.getPhaseId();
		System.out.println("phaseId= " + phaseId);
		phaseAssessment.setPhase(AppraisalAssembler.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

		long templateId = employeePhaseAssignment.getTemplateId();
		System.out.println("templateId= " + templateId);
		Template template = templateDataRepository.findById(templateId);
		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		phaseAssessment.setTemplateHeaders(TemplateAssembler.getTemplateHeaderDtoList(templateHeaders));

		List<PhaseAssessHeader> phaseAssessHeaders = phaseAssessmentHeaderDataRepository.findByAssignIdOrderByStatusAsc(assignmentId);
		phaseAssessment.setPhaseAssessmentHeaders(AssessmentAssembler.getHeaderDtoList(phaseAssessHeaders));
		return phaseAssessment;
	}

	@Transactional
	public void save(PhaseAssessHeaderDto phaseAssessmentHeaderDto)
			throws ServiceException {
		System.out.println("save(" + phaseAssessmentHeaderDto + ")");
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int employeeId = employeePhaseAssignment.getEmployeeId();
		int assessedBy = phaseAssessmentHeaderDto.getAssessedBy();
		if (assessedBy != employeeId) {
			throw new ServiceException("Assignment can only be saved by the employee to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		System.out.println("latestHeader=" + latestHeader);
		if (latestHeader != null) {
			PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
			if (latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
					&& latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
				throw new ServiceException("The assessment form is not in a state to modify now.");
			}
		}
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = AssessmentAssembler.getHeader(phaseAssessmentHeaderDto);
		System.out.println("phaseAssessHeader=" + phaseAssessHeader);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println("saved= " + saved);
		// Change assignment status to 20
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void submit(PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
		System.out.println("submit(" + phaseAssessmentHeaderDto + ")");
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int employeeId = employeePhaseAssignment.getEmployeeId();
		int assessedBy = phaseAssessmentHeaderDto.getAssessedBy();
		if (assessedBy != employeeId) {
			throw new ServiceException("Assignment can only be submitted by the employee to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		System.out.println("latestHeader=" + latestHeader);
		if (latestHeader != null) {
			PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
			if (latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
					&& latestStatus != PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
				throw new ServiceException("The assessment form is not in a state to submit now.");
			}
		}
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = AssessmentAssembler.getHeader(phaseAssessmentHeaderDto);
		System.out.println("phaseAssessHeader=" + phaseAssessHeader);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println("saved= " + saved);
		// Change assignment status to 30
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void review(PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
		System.out.println("review(" + phaseAssessmentHeaderDto + ")");
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		int assessedBy = phaseAssessmentHeaderDto.getAssessedBy();
		if (assessedBy != assignedBy) {
			throw new ServiceException("Assignment can only be reviewed by the manager to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		System.out.println("latestHeader=" + latestHeader);
		if (latestHeader == null) {
			throw new ServiceException("The assessment form is not in a state to review now.");
		}
		PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
		if (latestStatus != PhaseAssignmentStatus.MANAGER_REVIEW_PENDING
				&& latestStatus != PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
			throw new ServiceException("Review can only be done when the employee appraisal form is submitted.");
		}
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = AssessmentAssembler.getHeader(phaseAssessmentHeaderDto);
		System.out.println("phaseAssessHeader=" + phaseAssessHeader);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println("saved= " + saved);
		// Change assignment status to 40
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void freeze(PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
		System.out.println("freeze(" + phaseAssessmentHeaderDto + ")");
		long assignmentId = phaseAssessmentHeaderDto.getAssignId();
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		int assessedBy = phaseAssessmentHeaderDto.getAssessedBy();
		if (assessedBy != assignedBy) {
			throw new ServiceException("Assignment can only be concluded by the manager to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		PhaseAssessHeader latestHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		System.out.println("latestHeader=" + latestHeader);
		if (latestHeader == null) {
			throw new ServiceException("The assessment form is not in a state to freeze now.");
		}
		PhaseAssignmentStatus latestStatus = PhaseAssignmentStatus.get(latestHeader.getStatus());
		if (latestStatus != PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
			throw new ServiceException("Freeze can only be done when the manager review is completed.");
		}
		phaseAssessmentHeaderDto.setStatus(PhaseAssignmentStatus.CONCLUDED.getCode());
		ValidationUtil.validate(phaseAssessmentHeaderDto);
		PhaseAssessHeader phaseAssessHeader = AssessmentAssembler.getHeader(phaseAssessmentHeaderDto);
		System.out.println("phaseAssessHeader=" + phaseAssessHeader);
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		System.out.println("saved= " + saved);
		// Change assignment status to 50
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.CONCLUDED.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}


}
