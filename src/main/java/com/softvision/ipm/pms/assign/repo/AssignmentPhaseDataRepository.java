package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;

public interface AssignmentPhaseDataRepository extends CrudRepository<EmployeePhaseAssignment, Long> {

	List<EmployeePhaseAssignment> findAll();

	EmployeePhaseAssignment findByPhaseIdAndTemplateIdAndEmployeeId(Integer phaseId, Long templateId, Integer employeeId);

}
