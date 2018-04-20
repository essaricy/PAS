package com.softvision.ipm.pms.assign.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assign.entity.CycleAssignment;

public interface CycleAssignmentDataRepository extends CrudRepository<CycleAssignment, Long> {

	List<CycleAssignment> findAll();

	CycleAssignment findById(Long id);

	CycleAssignment findByCycleIdAndEmployeeId(Integer cycleId, Integer employeeId);

	CycleAssignment findByCycleIdAndEmployeeIdAndStatus(Integer cycleId, Integer employeeId, Integer code);

}
