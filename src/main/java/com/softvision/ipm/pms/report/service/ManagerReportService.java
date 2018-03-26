package com.softvision.ipm.pms.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.report.model.PhasewiseEmployeeStatusCountDto;
import com.softvision.ipm.pms.report.repo.ManagerReportRepository;

@Service
public class ManagerReportService {

	@Autowired private ManagerReportRepository managerReportRepository;

	public List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCounts(int requestedEmplyeeId) {
		List<PhasewiseEmployeeStatusCountDto> finalCounts = new ArrayList<PhasewiseEmployeeStatusCountDto>();
		List<PhasewiseEmployeeStatusCountDto> phasewiseEmployeeStatusCounts = managerReportRepository.getPhasewiseEmployeeStatusCounts(requestedEmplyeeId);
		
		/*PhasewiseEmployeeStatusCountDto notAssignedCount = new PhasewiseEmployeeStatusCountDto();
		notAssignedCount.setPhaseAssignmentStatus(PhaseAssignmentStatus.NOT_INITIATED);
		notAssignedCount.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.NOT_INITIATED));
		finalCounts.add(notAssignedCount);*/

		PhasewiseEmployeeStatusCountDto employeeSubmissionPendingCounts = new PhasewiseEmployeeStatusCountDto();
		employeeSubmissionPendingCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.SELF_APPRAISAL_PENDING);
		employeeSubmissionPendingCounts.setCount(get(phasewiseEmployeeStatusCounts,
				PhaseAssignmentStatus.SELF_APPRAISAL_PENDING, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED));
		finalCounts.add(employeeSubmissionPendingCounts);

		PhasewiseEmployeeStatusCountDto managerReviewPendingCounts = new PhasewiseEmployeeStatusCountDto();
		managerReviewPendingCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.MANAGER_REVIEW_PENDING);
		managerReviewPendingCounts.setCount(get(phasewiseEmployeeStatusCounts,
				PhaseAssignmentStatus.MANAGER_REVIEW_PENDING, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED));
		finalCounts.add(managerReviewPendingCounts);

		PhasewiseEmployeeStatusCountDto employeeAckPendingCounts = new PhasewiseEmployeeStatusCountDto();
		employeeAckPendingCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED);
		employeeAckPendingCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED));
		finalCounts.add(employeeAckPendingCounts);

		PhasewiseEmployeeStatusCountDto escalatedCounts = new PhasewiseEmployeeStatusCountDto();
		escalatedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
		escalatedCounts.setCount(get(phasewiseEmployeeStatusCounts, PhaseAssignmentStatus.EMPLOYEE_ESCALATED));
		finalCounts.add(escalatedCounts);

		/*PhasewiseEmployeeStatusCountDto concludedCounts = new PhasewiseEmployeeStatusCountDto();
		concludedCounts.setPhaseAssignmentStatus(PhaseAssignmentStatus.CONCLUDED);
		concludedCounts.setCount(get(phasewiseEmployeeStatusCounts,
				PhaseAssignmentStatus.CONCLUDED, PhaseAssignmentStatus.CONCLUDED));
		finalCounts.add(concludedCounts);*/

		System.out.println("finalCounts= " + finalCounts);
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
