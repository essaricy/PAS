package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;

public interface CycleAssignmentDataRepository extends CrudRepository<EmployeeCycleAssignment, Long> {

	List<EmployeeCycleAssignment> findAll();

	EmployeeCycleAssignment findById(Long id);

	EmployeeCycleAssignment findFirstByTemplateId(Long id);

	EmployeeCycleAssignment findByCycleIdAndEmployeeId(Integer cycleId, Integer employeeId);

	EmployeeCycleAssignment findByCycleIdAndEmployeeIdAndTemplateId(Integer cycleId, Integer employeeId, Long templateId);

	EmployeeCycleAssignment findByCycleIdAndEmployeeIdAndStatus(Integer cycleId, Integer employeeId, Integer code);

}
