package com.softvision.ipm.pms.assessment.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.assessment.entity.EmployeeAssign;


public interface EmployeeAssignRepository extends CrudRepository<EmployeeAssign, Long>{
	
	List<EmployeeAssign> findAll();

	EmployeeAssign findById(@Param("id") Long id);

}
