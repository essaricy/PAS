package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;

public interface PhaseAssignmentDataRepository extends CrudRepository<EmployeePhaseAssignment, Long> {

	List<EmployeePhaseAssignment> findAll();

	EmployeePhaseAssignment findById(Long id);

	EmployeePhaseAssignment findByPhaseIdAndEmployeeIdAndStatus(Integer phaseId, Integer employeeId, int status);

	EmployeePhaseAssignment findByPhaseIdAndTemplateIdAndEmployeeId(Integer phaseId, Long templateId, Integer employeeId);

}
