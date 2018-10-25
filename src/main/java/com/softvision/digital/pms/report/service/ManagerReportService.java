package com.softvision.digital.pms.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.report.model.PhasewiseEmployeeStatusCountDto;
import com.softvision.digital.pms.report.repo.ManagerReportRepository;

@Service
public class ManagerReportService {

	@Autowired private ManagerReportRepository managerReportRepository;

	public List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCounts(int requestedEmplyeeId) {
		List<PhasewiseEmployeeStatusCountDto> finalCounts = new ArrayList<>();
		List<PhasewiseEmployeeStatusCountDto> phasewiseEmployeeStatusCounts = managerReportRepository.getPhasewiseEmployeeStatusCounts(requestedEmplyeeId);

		PhasewiseEmployeeStatusCountDto notInitiatedCounts = new PhasewiseEmployeeStatusCountDto();
		notInitiatedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.NOT_INITIATED);
		notInitiatedCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.NOT_INITIATED));
		finalCounts.add(notInitiatedCounts);

		PhasewiseEmployeeStatusCountDto employeeSubmissionPendingCounts = new PhasewiseEmployeeStatusCountDto();
		employeeSubmissionPendingCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.SELF_APPRAISAL_PENDING);
		employeeSubmissionPendingCounts.setCount(get(phasewiseEmployeeStatusCounts,
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED));
		finalCounts.add(employeeSubmissionPendingCounts);

		PhasewiseEmployeeStatusCountDto revertedCounts = new PhasewiseEmployeeStatusCountDto();
		revertedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED);
		revertedCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED));
		finalCounts.add(revertedCounts);

		PhasewiseEmployeeStatusCountDto managerReviewPendingCounts = new PhasewiseEmployeeStatusCountDto();
		managerReviewPendingCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING);
		managerReviewPendingCounts.setCount(get(phasewiseEmployeeStatusCounts,
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED));
		finalCounts.add(managerReviewPendingCounts);

		PhasewiseEmployeeStatusCountDto employeeAckPendingCounts = new PhasewiseEmployeeStatusCountDto();
		employeeAckPendingCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED);
		employeeAckPendingCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED));
		finalCounts.add(employeeAckPendingCounts);

		PhasewiseEmployeeStatusCountDto agreedCounts = new PhasewiseEmployeeStatusCountDto();
		agreedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.EMPLOYEE_AGREED);
		agreedCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.EMPLOYEE_AGREED));
		finalCounts.add(agreedCounts);

		PhasewiseEmployeeStatusCountDto escalatedCounts = new PhasewiseEmployeeStatusCountDto();
		escalatedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		escalatedCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.EMPLOYEE_ESCALATED));
		finalCounts.add(escalatedCounts);

		PhasewiseEmployeeStatusCountDto concludedCounts = new PhasewiseEmployeeStatusCountDto();
		concludedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.CONCLUDED);
		concludedCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.CONCLUDED));
		finalCounts.add(concludedCounts);
		return finalCounts;
	}

	private int get(List<PhasewiseEmployeeStatusCountDto> phasewiseEmployeeStatusCounts,
			PhaseAssignmentStatus... phaseAssignmentStatusList) {
		int count=0;
		for (PhasewiseEmployeeStatusCountDto phasewiseEmployeeStatusCountDto : phasewiseEmployeeStatusCounts) {
			PhaseAssignmentStatus gotPhaseAssignmentStatus = phasewiseEmployeeStatusCountDto.getPhaseAssignmentStatus();
			for (PhaseAssignmentStatus phaseAssignmentStatus : phaseAssignmentStatusList) {
				if (gotPhaseAssignmentStatus == phaseAssignmentStatus) {
					count+=phasewiseEmployeeStatusCountDto.getCount();
				}
			}
		}
		return count;
	}

}
