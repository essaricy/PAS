package com.softvision.digital.pms.assign.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.assign.entity.CycleAssignment;

public interface CycleAssignmentDataRepository extends CrudRepository<CycleAssignment, Long> {

	List<CycleAssignment> findAll();

	Optional<CycleAssignment> findById(Long id);

	Optional<CycleAssignment> findByCycleIdAndEmployeeId(Integer cycleId, Integer employeeId);

	Optional<CycleAssignment> findByCycleIdAndEmployeeIdAndStatus(Integer cycleId, Integer employeeId, Integer code);

}
