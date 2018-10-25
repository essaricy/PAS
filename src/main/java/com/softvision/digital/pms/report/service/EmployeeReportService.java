package com.softvision.digital.pms.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.digital.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.digital.pms.appraisal.service.AppraisalService;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.digital.pms.assign.model.PhaseAssignmentDto;
import com.softvision.digital.pms.assign.repo.EmployeeAssignmentRepository;

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
				List<EmployeePhaseAssignmentDto> employeeAssignmentsOfPhase = employeeAssignmentRepository.getAssignmentsByEmployeeIdByPhase(employeeId, cycleId, phase.getId());
				if (employeeAssignmentsOfPhase == null || employeeAssignmentsOfPhase.isEmpty()) {
					employeeAssignmentsOfPhase = new ArrayList<>();
					EmployeePhaseAssignmentDto employeeAssignment = new EmployeePhaseAssignmentDto();
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
