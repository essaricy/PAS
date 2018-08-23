package com.softvision.ipm.pms;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assess.entity.AssessDetail;
import com.softvision.ipm.pms.assess.entity.AssessHeader;
import com.softvision.ipm.pms.assess.repo.AssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.report.service.AdminReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@Ignore
public class ScoreSummaryReportTest {

	private static final String CSV_HEADER = "Employee Id, EmployeeName, Manager Id, Manager Name, Status, Employee Score, Manager Score";

	//private static final String CSV_LINE = "{0},{1},{2},{3},{4},{5},{6}";

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AdminReportService adminReportService;

	@Autowired private AssessmentHeaderDataRepository assessmentHeaderDataRepository;

	@Test
	public void pullCurrentCycle() {
		/*activeCycle.getPhases().stream().forEach(phase -> {
		Integer phaseId = phase.getId();
		List<EmployeePhaseAssignmentDto> phaseAssignments = adminReportService.getAllAssignments(cycleId, phaseId);
		
		});*/

		AppraisalCycle activeCycle = appraisalRepository.getActiveCycle();
		Integer cycleId = activeCycle.getId();
		System.out.println(activeCycle);

		Integer phaseId = activeCycle.getPhases().get(0).getId();
		List<EmployeePhaseAssignmentDto> phaseAssignments = adminReportService.getAllAssignments(cycleId, phaseId);

		log.info(CSV_HEADER);

		StringBuffer csvLines = new StringBuffer();
		csvLines.append(CSV_HEADER).append("\n");

		phaseAssignments.stream().forEach(phaseAssignment -> {
			long assignmentId = phaseAssignment.getAssignmentId();
			EmployeeDto assignedTo = phaseAssignment.getAssignedTo();
			EmployeeDto assignedBy = phaseAssignment.getAssignedBy();
			PhaseAssignmentStatus status = PhaseAssignmentStatus.get(phaseAssignment.getStatus());

			String csvLine = null;
			double selfRating=-1;
			double managerRating=-1;

			if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING
					|| status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
				List<AssessHeader> assessHeaders = assessmentHeaderDataRepository.findByAssignIdOrderByStageAsc(assignmentId);
				selfRating=getRating(assessHeaders, 0);
				managerRating=-1;
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED
					|| status == PhaseAssignmentStatus.EMPLOYEE_AGREED
					|| status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED
					|| status == PhaseAssignmentStatus.CONCLUDED) {
				List<AssessHeader> assessHeaders = assessmentHeaderDataRepository.findByAssignIdOrderByStageAsc(assignmentId);
				selfRating=getRating(assessHeaders, 0);
				managerRating=getRating(assessHeaders, 1);
			}
			csvLine = assignedTo.getEmployeeId() + "," + assignedTo.getFullName() + "," +
					assignedBy.getEmployeeId() + "," + assignedBy.getFullName() + "," + 
					status.getName() + "," + 
					((selfRating<0)? "-" : selfRating + ",") + 
					((managerRating<0)? "-" : managerRating);

			log.info(csvLine);
			csvLines.append(csvLine).append("\n");
		});
		System.out.println("Writing to file");
		File file = new File("C:\\Users\\srikanth.kumar\\Desktop\\PMS-scores.csv");
		try {
			FileUtils.writeStringToFile(file, csvLines.toString());
			System.out.println("Written to file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double getRating(List<AssessHeader> assessHeaders, int stage) {
		for (AssessHeader assessHeader : assessHeaders) {
			if (stage == assessHeader.getStage()) {
				double goalsSum = 0;
				List<AssessDetail> assessDetails = assessHeader.getAssessDetails();
				for (AssessDetail assessDetail : assessDetails) {
					double score = assessDetail.getScore();
					goalsSum = goalsSum + score;
				}
				return goalsSum;
			}
		}
		return -1;
	}

}
