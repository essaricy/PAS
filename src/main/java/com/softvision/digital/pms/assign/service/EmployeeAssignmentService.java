package com.softvision.digital.pms.assign.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.digital.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.digital.pms.appraisal.repo.AppraisalRepository;
import com.softvision.digital.pms.assign.model.EmployeeCycleAssignmentDto;
import com.softvision.digital.pms.assign.model.PhaseAssignmentDto;
import com.softvision.digital.pms.assign.repo.EmployeeAssignmentRepository;

@Service
public class EmployeeAssignmentService {

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private EmployeeAssignmentRepository employeeAssignmentRepository;

	@Autowired private AppraisalMapper appraisalMapper;

	public List<EmployeeCycleAssignmentDto> getAllCycles(int employeeId) {
		List<EmployeeCycleAssignmentDto> cycleAssignments = new ArrayList<>();
		List<AppraisalCycle> allCycles = appraisalCycleDataRepository.findAllByOrderByStartDateDesc();
		for (AppraisalCycle cycle : allCycles) {
			int cycleId = cycle.getId();
			AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
			if (appraisalCycleStatus == AppraisalCycleStatus.DRAFT) {
			    continue;
			}
			EmployeeCycleAssignmentDto cycleAssignment = new EmployeeCycleAssignmentDto();
			cycleAssignment.setCycle(appraisalMapper.getCycle(cycle));
			cycleAssignment.setEmployeeAssignment(employeeAssignmentRepository.getAssignmentsByEmployeeIdByCycle(employeeId, cycleId));
			List<PhaseAssignmentDto> phaseAssignments = new ArrayList<>();
			cycleAssignment.setPhaseAssignments(phaseAssignments);
			List<AppraisalPhase> phases = cycle.getPhases();
			for (AppraisalPhase phase : phases) {
				PhaseAssignmentDto phaseAssignment = new PhaseAssignmentDto();
				phaseAssignment.setPhase(appraisalMapper.getPhase(phase));
				phaseAssignment.setEmployeeAssignments(employeeAssignmentRepository.getAssignmentsByEmployeeIdByPhase(employeeId, cycleId, phase.getId()));
				phaseAssignments.add(phaseAssignment);
			}
			cycleAssignments.add(cycleAssignment);
		}
		return cycleAssignments;
	}

	public EmployeeCycleAssignmentDto getActiveCycle(int employeeId) {
		Optional<AppraisalCycle> optional = appraisalRepository.getActiveCycle();
		if (!optional.isPresent()) {
			return null;
		}
		AppraisalCycle cycle = optional.get();
		int cycleId = cycle.getId();
		EmployeeCycleAssignmentDto cycleAssignment = new EmployeeCycleAssignmentDto();
		cycleAssignment.setCycle(appraisalMapper.getCycle(cycle));
		cycleAssignment.setEmployeeAssignment(employeeAssignmentRepository.getAssignmentsByEmployeeIdByCycle(employeeId, cycleId));
		List<PhaseAssignmentDto> phaseAssignments = new ArrayList<>();
		cycleAssignment.setPhaseAssignments(phaseAssignments);
		List<AppraisalPhase> phases = cycle.getPhases();
		for (AppraisalPhase phase : phases) {
			PhaseAssignmentDto phaseAssignment = new PhaseAssignmentDto();
			phaseAssignment.setPhase(appraisalMapper.getPhase(phase));
			phaseAssignment.setEmployeeAssignments(employeeAssignmentRepository.getAssignmentsByEmployeeIdByPhase(employeeId, cycleId, phase.getId()));
			phaseAssignments.add(phaseAssignment);
		}
		return cycleAssignment;
	}

}
