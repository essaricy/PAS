package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;

public interface PhaseAssignmentDataRepository extends CrudRepository<EmployeePhaseAssignment, Long> {

	List<EmployeePhaseAssignment> findAll();

	EmployeePhaseAssignment findById(Long id);

	EmployeeCycleAssignment findFirstByTemplateId(Long id);

	EmployeePhaseAssignment findByPhaseIdAndEmployeeIdAndStatus(Integer phaseId, Integer employeeId, int status);

	EmployeePhaseAssignment findByPhaseIdAndEmployeeIdAndTemplateId(Integer phaseId, Integer employeeId, Long templateId);

}
