package com.softvision.digital.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.assign.entity.PhaseAssignment;

public interface PhaseAssignmentDataRepository extends CrudRepository<PhaseAssignment, Long> {

	List<PhaseAssignment> findAll();

	PhaseAssignment findById(Long id);

	PhaseAssignment findFirstByTemplateId(Long id);

	PhaseAssignment findByPhaseIdAndEmployeeId(Integer phaseId, Integer employeeId);

	PhaseAssignment findByPhaseIdAndEmployeeIdAndStatus(Integer phaseId, Integer employeeId, int status);

	PhaseAssignment findByPhaseIdAndEmployeeIdAndTemplateId(Integer phaseId, Integer employeeId, Long templateId);

}
