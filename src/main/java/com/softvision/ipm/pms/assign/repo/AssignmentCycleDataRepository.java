package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;

public interface AssignmentCycleDataRepository extends CrudRepository<EmployeeCycleAssignment, Long> {

	List<EmployeeCycleAssignment> findAll();

	EmployeeCycleAssignment findByCycleIdAndTemplateIdAndEmployeeId(Integer cycleId, Long templateId, Integer employeeId);

	EmployeeCycleAssignment findByCycleIdAndEmployeeId(Integer cycleId, Integer employeeId);

}
