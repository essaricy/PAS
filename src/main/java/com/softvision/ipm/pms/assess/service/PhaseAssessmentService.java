package com.softvision.ipm.pms.assess.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assess.assembler.AssessmentAssembler;
import com.softvision.ipm.pms.assess.entity.CycleAssessDetail;
import com.softvision.ipm.pms.assess.entity.CycleAssessHeader;
import com.softvision.ipm.pms.assess.entity.PhaseAssessDetail;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.model.PhaseAssessDetailDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.repo.PhaseAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.AssignmentCycleDataRepository;
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

	@Autowired private CycleAssessmentService cycleAssessmentService;

	public PhaseAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId) throws ServiceException {
		PhaseAssessmentDto phaseAssessment = new PhaseAssessmentDto();
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);

		// Allow this form only to the employee and to the manager to whom its been assigned
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		int employeeId = employeePhaseAssignment.getEmployeeId();

		if (requestedEmployeeId != assignedBy
				&& requestedEmployeeId != employeeId) {
			throw new ServiceException("No allowed to view other's appraisal forms");
		}
		phaseAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeePhaseAssignment));

		int phaseId = employeePhaseAssignment.getPhaseId();
		phaseAssessment.setPhase(AppraisalAssembler.getPhase(appraisalPhaseDataRepository.findById(phaseId)));

		long templateId = employeePhaseAssignment.getTemplateId();
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
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		// Change assignment status to 20
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void submit(PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
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
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		// Change assignment status to 30
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void review(PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
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
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		// Change assignment status to 40
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);
	}

	@Transactional
	public void freeze(PhaseAssessHeaderDto phaseAssessmentHeaderDto) throws ServiceException {
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
		PhaseAssessHeader saved = phaseAssessmentHeaderDataRepository.save(phaseAssessHeader);
		// Change assignment status to 50
		employeePhaseAssignment.setStatus(PhaseAssignmentStatus.CONCLUDED.getCode());
		// Move assignment to next status
		assignmentPhaseDataRepository.save(employeePhaseAssignment);

		// Check if this assignment is the phase assignment for the employee for this cycle.
		List<AppraisalPhase> nextPhases = appraisalPhaseDataRepository.findNextPhases(employeePhaseAssignment.getPhaseId());
		if (nextPhases == null || nextPhases.isEmpty()) {
			// This is the last phase. Summarize the phases ratings and dump to appraisal cycle
			cycleAssessmentService.abridge(employeePhaseAssignment.getEmployeeId());
		}
	}

	private void summarizeCycle() {
			boolean concluded=false;
			//cycle_assess_header
			CycleAssessHeader cycleAssessHeader = new CycleAssessHeader();
			for (PhaseAssessHeaderDto phaseAssessHeaderDto : phaseAssessmentHeaders) {
				PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(phaseAssessHeaderDto.getStatus());
				if (phaseAssignmentStatus == PhaseAssignmentStatus.CONCLUDED) {
					concluded=true;
					cycleAssessHeader.setAssignId(phaseAssessHeaderDto.getAssignId());
					// STATUS = 10
					cycleAssessHeader.setStatus(CycleAssignmentStatus.ABRIDGED.getCode());
					// ASSESS_DATE = today
					cycleAssessHeader.setAssessDate(phaseAssessHeaderDto.getAssessDate());
					// ASSESSED_BY = last assigned by
					cycleAssessHeader.setAssessedBy(phaseAssessHeaderDto.getAssessedBy());

					List<PhaseAssessDetailDto> phaseAssessDetails = phaseAssessHeaderDto.getPhaseAssessDetails();
					if (phaseAssessDetails == null || phaseAssessDetails.isEmpty()) {
						throw new ServiceException("There are no phase assessments found to summarize");
					}
					List<CycleAssessDetail> cycleAssessDetails = new ArrayList<>();
					for (PhaseAssessDetailDto phaseAssessDetail : phaseAssessDetails) {
						CycleAssessDetail cycleAssessDetail = new CycleAssessDetail();
						//cycle_assess_detail
						double rating = phaseAssessDetail.getRating();
						// COMMENTS = Phase 1: rating, Phase 2: Rating, Phase 3: Rating
						phase.getName()
						cycleAssessDetail.setComments(comments);
						//cycleAssessDetail.setId(id);
						cycleAssessDetail.setRating(phaseAssessDetail.getRating());
						cycleAssessDetail.setScore(phaseAssessDetail.getScore());
						// TEMPLATE_HEADER_ID = template header id from phase-assign
						cycleAssessDetail.setTemplateHeaderId(phaseAssessDetail.getTemplateHeaderId());
						cycleAssessDetails.add(cycleAssessDetail);
					}
					cycleAssessHeader.setCycleAssessDetails(cycleAssessDetails);
					break;
				}
			}
			if (!concluded) {
				throw new ServiceException("There seems to be an assignment pending which needs to be concluded. Cannot abridge appraisal cycle.");
			}

		}
	}

}
