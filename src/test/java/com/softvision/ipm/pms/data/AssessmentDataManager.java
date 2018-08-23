package com.softvision.ipm.pms.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.appraisal.service.AppraisalService;
import com.softvision.digital.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.digital.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.digital.pms.assign.service.AssignmentService;
import com.softvision.digital.pms.assign.service.ManagerAssignmentService;
import com.softvision.digital.pms.employee.service.EmployeeService;
import com.softvision.digital.pms.template.service.TemplateService;

@Component
public class AssessmentDataManager implements AbstractDataManager {

	@Autowired AssignmentService assignmentService;

	@Autowired ManagerAssignmentService managerAssignmentService;

	@Autowired AppraisalService appraisalService;

	@Autowired TemplateService templateService;

	@Autowired EmployeeService employeeService;

	@Autowired CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Override
	public void clearData() throws Exception {
		clearManagerAssessments();
	}

	private void clearManagerAssessments() {
	}

	@Override
	public void loadData() throws Exception {
		/*AppraisalCycleDto activeCycle = appraisalService.getActiveCycle();
		List<EmployeeAssignmentDto> employeeAssignments = assignmentService.getAllEmployeeAssignmentsforCycle(activeCycle.getId());
		if (employeeAssignments != null && !employeeAssignments.isEmpty()) {
			for (EmployeeAssignmentDto employeeAssignment : employeeAssignments) {
				int status = employeeAssignment.getStatus();
				long assignmentId = employeeAssignment.getAssignmentId();
				int employeeId = employeeAssignment.getAssignedTo().getEmployeeId();
				int assignedBy = employeeAssignment.getAssignedBy().getEmployeeId();
				PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(status);

				switch (phaseAssignmentStatus) {
				case NOT_INITIATED:
					phaseAssessmentService.enablePhaseAppraisal(assignmentId, employeeId);
					break;
				case SELF_APPRAISAL_PENDING:
					AssessHeaderDto assessHeaderDto = new AssessHeaderDto();
					assessHeaderDto.setAssignId(assignmentId);
					assessHeaderDto.setAssessedBy(employeeId);
					assessHeaderDto.setAssessDate(new Date());
					List<AssessDetailDto> assessDetails = new ArrayList<>();
					AssessDetailDto assessDetail = new AssessDetailDto();
					//assessDetail.setRating(rating);
					//assessDetail.setScore(score);
					//assessDetail.setTemplateHeaderId(templateHeaderId);
					//assessDetail.setComments(comments);
					assessDetail.setId(0);
					assessDetails.add(assessDetail);
					assessHeaderDto.setAssessDetails(assessDetails);
					phaseAssessmentService.saveSelfAppraisal(assignmentId, employeeId, assessHeaderDto);
					break;
				default:
					break;
				}
			}
		}*/
	}

}
