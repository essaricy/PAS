package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;

public interface PhaseAssignmentDataRepository extends CrudRepository<PhaseAssignment, Long> {

	List<PhaseAssignment> findAll();

	PhaseAssignment findById(Long id);

	CycleAssignment findFirstByTemplateId(Long id);

	PhaseAssignment findByPhaseIdAndEmployeeIdAndStatus(Integer phaseId, Integer employeeId, int status);

	PhaseAssignment findByPhaseIdAndEmployeeIdAndTemplateId(Integer phaseId, Integer employeeId, Long templateId);

}
