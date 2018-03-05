package com.softvision.ipm.pms.assess.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assess.entity.EmployeePhaseAssessment;
import com.softvision.ipm.pms.assess.model.AssignmentAssessmentsDto;
import com.softvision.ipm.pms.assess.model.EmployeeAssessmentDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.repo.AssessmentPhaseDataRepository;
import com.softvision.ipm.pms.assign.assembler.AssignmentAssembler;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.AssignmentPhaseDataRepository;
import com.softvision.ipm.pms.goal.assembler.GoalAssembler;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class AssessmentService {

	@Autowired private AssignmentPhaseDataRepository assignmentPhaseDataRepository;

	@Autowired private AssessmentPhaseDataRepository assessmentDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	public PhaseAssessmentDto getByAssignment(long assignmentId) {
		PhaseAssessmentDto phaseAssessment = null;
		EmployeePhaseAssignment phaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
		System.out.println("phaseAssignment= " + phaseAssignment);
		if (phaseAssignment != null) {
			//AssignmentPhaseStatus assignmentPhaseStatus = AssignmentPhaseStatus.get(phaseAssignment.getStatus());
			List<EmployeePhaseAssessment> assessments = assessmentDataRepository.findByAssignIdOrderByAssessTypeAsc(assignmentId);

			phaseAssessment = new PhaseAssessmentDto();
			EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(assignmentId);
			phaseAssessment.setEmployeeAssignment(AssignmentAssembler.get(employeePhaseAssignment));

			long templateId = phaseAssignment.getTemplateId();
			Template template = templateDataRepository.findById(templateId);
			List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
			phaseAssessment.setTemplateHeaders(TemplateAssembler.getTemplateHeaderDtoList(templateHeaders));

			if (assessments == null || assessments.isEmpty()) {
				System.out.println("Its a new assessment");
				List<AssignmentAssessmentsDto> assignmentAssessments = new ArrayList<>();
				AssignmentAssessmentsDto assignmentAssessment = new AssignmentAssessmentsDto();
				List<EmployeeAssessmentDto> employeeAssessments = new ArrayList<>();
				for (TemplateHeader templateHeader : templateHeaders) {
					EmployeeAssessmentDto employeeAssessment = new EmployeeAssessmentDto();
					employeeAssessment.setGoal(GoalAssembler.getGoal(templateHeader.getGoal()));
					employeeAssessments.add(employeeAssessment);
				}
				assignmentAssessment.setEmployeeAssessments(employeeAssessments);
				assignmentAssessments.add(assignmentAssessment);
				phaseAssessment.setAssignmentAssessments(assignmentAssessments);
			} else {
				// existing assessment.
			}
		}
		return phaseAssessment;
	}

}
