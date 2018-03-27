package com.softvision.ipm.pms.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.PhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.EmployeeAssignmentRepository;

@Service
public class EmployeeReportService {

	@Autowired private AppraisalService appraisalService;

	@Autowired private EmployeeAssignmentRepository employeeAssignmentRepository;

	public List<PhaseAssignmentDto> getCurrentPhaseAssignment(int employeeId) {
		List<PhaseAssignmentDto> phaseAssignments = new ArrayList<>();
		AppraisalCycleDto cycle = appraisalService.getActiveCycle();
		if (cycle != null) {
			int cycleId = cycle.getId();
			List<AppraisalPhaseDto> phases = cycle.getPhases();
			for (AppraisalPhaseDto phase : phases) {
				PhaseAssignmentDto phaseAssignment = new PhaseAssignmentDto();
				phaseAssignment.setPhase(phase);
				List<EmployeeAssignmentDto> employeeAssignmentsOfPhase = employeeAssignmentRepository.getEmployeeAssignmentsOfPhase(employeeId, cycleId, phase.getId());
				if (employeeAssignmentsOfPhase == null || employeeAssignmentsOfPhase.isEmpty()) {
					employeeAssignmentsOfPhase = new ArrayList<>();
					EmployeeAssignmentDto employeeAssignment = new EmployeeAssignmentDto();
					employeeAssignment.setStatus(-1);
					employeeAssignmentsOfPhase.add(employeeAssignment);
				}
				phaseAssignment.setEmployeeAssignments(employeeAssignmentsOfPhase);
				phaseAssignments.add(phaseAssignment);
			}
		}
		return phaseAssignments;
	}

}
