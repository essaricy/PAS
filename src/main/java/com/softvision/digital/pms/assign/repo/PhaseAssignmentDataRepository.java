package com.softvision.digital.pms.assign.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.assign.entity.PhaseAssignment;

public interface PhaseAssignmentDataRepository extends CrudRepository<PhaseAssignment, Long> {

	List<PhaseAssignment> findAll();

	Optional<PhaseAssignment> findById(Long id);

	Optional<PhaseAssignment> findFirstByTemplateId(Long id);

	Optional<PhaseAssignment> findByPhaseIdAndEmployeeId(Integer phaseId, Integer employeeId);

	Optional<PhaseAssignment> findByPhaseIdAndEmployeeIdAndStatus(Integer phaseId, Integer employeeId, int status);

	Optional<PhaseAssignment> findByPhaseIdAndEmployeeIdAndTemplateId(Integer phaseId, Integer employeeId, Long templateId);

}
