package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;

public interface CycleAssignmentDataRepository extends CrudRepository<EmployeeCycleAssignment, Long> {

	List<EmployeeCycleAssignment> findAll();

	EmployeeCycleAssignment findById(Long id);

	EmployeeCycleAssignment findByCycleIdAndTemplateIdAndEmployeeId(Integer cycleId, Long templateId, Integer employeeId);

	EmployeeCycleAssignment findByCycleIdAndEmployeeId(Integer cycleId, Integer employeeId);

	EmployeeCycleAssignment findByCycleIdAndEmployeeIdAndStatus(Integer cycleId, Integer employeeId, Integer code);

}
