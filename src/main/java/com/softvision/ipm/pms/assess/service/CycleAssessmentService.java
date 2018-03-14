package com.softvision.ipm.pms.assess.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assess.assembler.CycleAssessmentAssembler;
import com.softvision.ipm.pms.assess.entity.CycleAssessDetail;
import com.softvision.ipm.pms.assess.entity.CycleAssessHeader;
import com.softvision.ipm.pms.assess.entity.PhaseAssessDetail;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.model.CycleAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.CycleAssessmentDto;
import com.softvision.ipm.pms.assess.repo.CycleAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assess.repo.PhaseAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class CycleAssessmentService {

	private static final String CYCLE_SUMMARY_FORMAT= "{0} : Rating= {1}, Score= {2}\n";

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private PhaseAssessmentHeaderDataRepository phaseAssessmentHeaderDataRepository;

	@Autowired private CycleAssessmentHeaderDataRepository cycleAssessmentHeaderDataRepository;

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Transactional
	public boolean abridge(int employeeId) {
		System.out.println("##################################### abridge " + employeeId);
		int lastPhaseAssignedBy=0;
		AppraisalCycle activeCycle = appraisalRepository.getActiveCycle();
		Integer cycleId = activeCycle.getId();
		List<AppraisalPhase> phases = activeCycle.getPhases();

		EmployeeCycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
		if (employeeCycleAssignment == null) {
			return false;
		}
		Long cycleAssignmentId = employeeCycleAssignment.getId();
		System.out.println("cycleAssignmentId=" + cycleAssignmentId);

		CycleAssessHeader cycleAssessHeader = cycleAssessmentHeaderDataRepository.findByAssignId(cycleAssignmentId);
		System.out.println("CycleAssessHeader does not exist. GOOD");
		if (cycleAssessHeader != null) {
			return false;
		}
		List<CycleAssessDetail> cycleAssessDetails = new ArrayList<>();
		// Get phase assignments for each phase by employee id and phase id
		for (AppraisalPhase phase : phases) {
			System.out.println();
			int phaseId = phase.getId();
			System.out.println("phaseId=" + phaseId + ", employeeId=" + employeeId + ", status=" + PhaseAssignmentStatus.CONCLUDED.getCode());
			EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository
					.findByPhaseIdAndEmployeeIdAndStatus(phaseId, employeeId, PhaseAssignmentStatus.CONCLUDED.getCode());
			System.out.println("Concluded employeePhaseAssignment for " + phase.getName() + " = " + employeePhaseAssignment);
			if (employeePhaseAssignment == null) {
				// There is no CONCLUDED assignment for this phase
				// This assignment phase is not yet concluded. cannot abridge
				return false;
			}
			Long assignmentId = employeePhaseAssignment.getId();
			System.out.println("assignmentId = " + assignmentId);
			PhaseAssessHeader concludedPhaseAssessHeader = phaseAssessmentHeaderDataRepository.findFirstByAssignIdAndStatus(assignmentId, PhaseAssignmentStatus.CONCLUDED.getCode());
			System.out.println("concluded PhaseAssessHeader = " + concludedPhaseAssessHeader);
			if (concludedPhaseAssessHeader == null) {
				// CONCLUDED Header is not found
				return false;
			}
			lastPhaseAssignedBy = concludedPhaseAssessHeader.getAssessedBy();
			List<PhaseAssessDetail> phaseAssessDetails = concludedPhaseAssessHeader.getPhaseAssessDetails();
			for (PhaseAssessDetail phaseAssessDetail : phaseAssessDetails) {
				double rating = phaseAssessDetail.getRating();
				double score = phaseAssessDetail.getScore();
				long templateHeaderId = phaseAssessDetail.getTemplateHeaderId();
				CycleAssessDetail cycleAssessDetail = getByTemplateHeaderId(cycleAssessDetails, templateHeaderId);
				String summary = MessageFormat.format(CYCLE_SUMMARY_FORMAT, phase.getName(), rating, score);

				if (cycleAssessDetail == null) {
					cycleAssessDetail = new CycleAssessDetail();
					cycleAssessDetail.setRating(rating);
					cycleAssessDetail.setScore(score);
					cycleAssessDetail.setTemplateHeaderId(templateHeaderId);
					cycleAssessDetail.setComments(summary);
					cycleAssessDetails.add(cycleAssessDetail);
				} else {
					cycleAssessDetail.setRating(cycleAssessDetail.getRating() + rating);
					cycleAssessDetail.setScore(cycleAssessDetail.getScore() + score);
					cycleAssessDetail.setComments(cycleAssessDetail.getComments() + summary);
				}
			}
		}
		int numberOfPhases = phases.size();
		for (CycleAssessDetail cycleAssessDetail : cycleAssessDetails) {
			cycleAssessDetail.setRating(cycleAssessDetail.getRating()/numberOfPhases);
			cycleAssessDetail.setScore(cycleAssessDetail.getScore()/numberOfPhases);
		}
		cycleAssessHeader = new CycleAssessHeader();
		cycleAssessHeader.setAssignId(cycleAssignmentId);
		cycleAssessHeader.setStatus(CycleAssignmentStatus.ABRIDGED.getCode());
		cycleAssessHeader.setAssessDate(new Date());
		cycleAssessHeader.setAssessedBy(lastPhaseAssignedBy); // last phase assigned by
		cycleAssessHeader.setCycleAssessDetails(cycleAssessDetails);
		CycleAssessHeader saved = cycleAssessmentHeaderDataRepository.save(cycleAssessHeader);
		System.out.println();
		System.out.println("saved= " + saved);
		// Update employeeCycleAssignment status to 10
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.ABRIDGED.getCode());
		cycleAssignmentDataRepository.save(employeeCycleAssignment);
		return true;
	}

	public CycleAssessmentDto getByAssignment(long assignmentId, int requestedEmployeeId) throws ServiceException {
		CycleAssessmentDto cycleAssessment = new CycleAssessmentDto();
		EmployeeCycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(assignmentId);
		
		// Allow this form only to the employee and to the manager to whom its been assigned
		int assignedBy = employeeCycleAssignment.getAssignedBy();
		// TODO: Allow it to employee?
		//int employeeId = employeeCycleAssignment.getEmployeeId();
		if (requestedEmployeeId != assignedBy) {
			throw new ServiceException("No allowed to view appraisal form");
		}
		cycleAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeeCycleAssignment));

		int cycleId = employeeCycleAssignment.getCycleId();
		cycleAssessment.setCycle(AppraisalAssembler.getCycle(appraisalCycleDataRepository.findById(cycleId)));

		long templateId = employeeCycleAssignment.getTemplateId();
		Template template = templateDataRepository.findById(templateId);
		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		cycleAssessment.setTemplateHeaders(TemplateAssembler.getTemplateHeaderDtoList(templateHeaders));

		List<CycleAssessHeader> cycleAssessHeaders = cycleAssessmentHeaderDataRepository.findByAssignIdOrderByStatusAsc(assignmentId);
		cycleAssessment.setCycleAssessmentHeaders(CycleAssessmentAssembler.getCycleAssessHeaderDtos(cycleAssessHeaders));
		return cycleAssessment;
	}

	private CycleAssessDetail getByTemplateHeaderId(List<CycleAssessDetail> cycleAssessDetails, long templateHeaderId) {
		for (CycleAssessDetail cycleAssessDetail : cycleAssessDetails) {
			if (templateHeaderId == cycleAssessDetail.getTemplateHeaderId()) {
				return cycleAssessDetail;
			}
		}
		return null;
	}

	@Transactional
	public void review(CycleAssessHeaderDto cycleAssessHeaderDto) throws ServiceException {
		long assignmentId = cycleAssessHeaderDto.getAssignId();
		EmployeeCycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(assignmentId);
		if (employeeCycleAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int assignedBy = employeeCycleAssignment.getAssignedBy();
		int assessedBy = cycleAssessHeaderDto.getAssessedBy();
		if (assessedBy != assignedBy) {
			throw new ServiceException("Assignment can only be reviewed by the manager to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		CycleAssessHeader latestHeader = cycleAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		if (latestHeader == null) {
			throw new ServiceException("The assessment form is not in a state to review now.");
		}
		CycleAssignmentStatus latestStatus = CycleAssignmentStatus.get(latestHeader.getStatus());
		if (latestStatus != CycleAssignmentStatus.MANAGER_REVIEW_PENDING
				&& latestStatus != CycleAssignmentStatus.MANAGER_REVIEW_SAVED) {
			throw new ServiceException("Review can only be done when the employee appraisal form is submitted.");
		}
		cycleAssessHeaderDto.setStatus(CycleAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		ValidationUtil.validate(cycleAssessHeaderDto);
		CycleAssessHeader cycleAssessHeader = CycleAssessmentAssembler.getCycleAssessHeader(cycleAssessHeaderDto);
		CycleAssessHeader saved = cycleAssessmentHeaderDataRepository.save(cycleAssessHeader);
		System.out.println(saved);
		// Change assignment status to 40
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		// Move assignment to next status
		cycleAssignmentDataRepository.save(employeeCycleAssignment);
	}

	@Transactional
	public void reviewAndConclude(CycleAssessHeaderDto cycleAssessmentHeaderDto) throws ServiceException {
		long assignmentId = cycleAssessmentHeaderDto.getAssignId();
		int assessedBy = cycleAssessmentHeaderDto.getAssessedBy();

		validateBeforeConclude(assignmentId, assessedBy);
		// Save the form first
		cycleAssessmentHeaderDto.setStatus(CycleAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());
		ValidationUtil.validate(cycleAssessmentHeaderDto);
		CycleAssessHeader cycleAssessHeader = CycleAssessmentAssembler.getCycleAssessHeader(cycleAssessmentHeaderDto);
		CycleAssessHeader saved = cycleAssessmentHeaderDataRepository.save(cycleAssessHeader);
		System.out.println(saved);
		// CONCLUDE the assignment
		conclude(assignmentId, assessedBy);
	}

	@Transactional
	public void conclude(long assignmentId, int fromEmployeeId) throws ServiceException {
		validateBeforeConclude(assignmentId, fromEmployeeId);

		EmployeeCycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(assignmentId);
		CycleAssessHeader latestHeader = cycleAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);

		// Change assignment status to CONCLUDED
		boolean changed = managerAssignmentRepository.changeStatus(assignmentId, CycleAssignmentStatus.CONCLUDED.getCode());
		if (!changed) {
			throw new ServiceException("Unable to CONCLUDE this assignment");
		}
		System.out.println("######################## assignment (" + assignmentId + ") status changed with status " + CycleAssignmentStatus.CONCLUDED.getCode());
		employeeCycleAssignment = cycleAssignmentDataRepository
				.findByCycleIdAndEmployeeIdAndStatus(employeeCycleAssignment.getCycleId(), employeeCycleAssignment.getEmployeeId(), CycleAssignmentStatus.CONCLUDED.getCode());
		System.out.println("employeeCycleAssignment after save=" + employeeCycleAssignment);

		// Change last assessment status to CONCLUDED
		latestHeader.setStatus(CycleAssignmentStatus.CONCLUDED.getCode());
		CycleAssessHeader saved = cycleAssessmentHeaderDataRepository.save(latestHeader);
		System.out.println("######################## assessment status changed with status " + saved.getStatus());
	}

	private void validateBeforeConclude(long assignmentId, int assessedBy) throws ServiceException {
		EmployeeCycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(assignmentId);
		if (employeeCycleAssignment == null) {
			throw new ServiceException("No such assignment");
		}
		// Can be saved by the same person whom it has been assigned to.
		int assignedBy = employeeCycleAssignment.getAssignedBy();
		if (assessedBy != assignedBy) {
			throw new ServiceException("Assignment can only be concluded by the manager to whom its been assigned to");
		}
		// Check the latest assessment. Status must be self-appraisal pending.
		CycleAssessHeader latestHeader = cycleAssessmentHeaderDataRepository.findFirstByAssignIdOrderByStatusDesc(assignmentId);
		if (latestHeader == null) {
			throw new ServiceException("The assessment form is not in a state to CONCLUDE now.");
		}
		CycleAssignmentStatus latestStatus = CycleAssignmentStatus.get(latestHeader.getStatus());
		if (latestStatus != CycleAssignmentStatus.MANAGER_REVIEW_SAVED) {
			throw new ServiceException("CONCLUDE can only be done when the manager review is completed.");
		}
	}

}
