package com.softvision.ipm.pms.assess.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assess.entity.CycleAssessDetail;
import com.softvision.ipm.pms.assess.entity.CycleAssessHeader;
import com.softvision.ipm.pms.assess.entity.PhaseAssessDetail;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.repo.CycleAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assess.repo.PhaseAssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.AssignmentCycleDataRepository;
import com.softvision.ipm.pms.assign.repo.AssignmentPhaseDataRepository;

@Service
public class CycleAssessmentService {

	private static final String CYCLE_SUMMARY_FORMAT= "{0} : Rating= {1}, Score= {2}\n";

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private AssignmentCycleDataRepository assignmentCycleDataRepository;

	@Autowired private AssignmentPhaseDataRepository assignmentPhaseDataRepository;

	@Autowired private PhaseAssessmentHeaderDataRepository phaseAssessmentHeaderDataRepository;

	@Autowired private CycleAssessmentHeaderDataRepository cycleAssessmentHeaderDataRepository;

	@Transactional
	public boolean abridge(int employeeId) {
		System.out.println("##################################### abridge " + employeeId);
		int lastPhaseAssignedBy=0;
		AppraisalCycle activeCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Integer cycleId = activeCycle.getId();
		List<AppraisalPhase> phases = activeCycle.getPhases();

		EmployeeCycleAssignment employeeCycleAssignment = assignmentCycleDataRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
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
			EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository
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
		assignmentCycleDataRepository.save(employeeCycleAssignment);
		return true;
	}

	private CycleAssessDetail getByTemplateHeaderId(List<CycleAssessDetail> cycleAssessDetails, long templateHeaderId) {
		for (CycleAssessDetail cycleAssessDetail : cycleAssessDetails) {
			if (templateHeaderId == cycleAssessDetail.getTemplateHeaderId()) {
				return cycleAssessDetail;
			}
		}
		return null;
	}

}
