package com.softvision.ipm.pms.assessment.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.softvision.ipm.pms.assessment.model.EmployeeAssign;

@RepositoryRestResource(collectionResourceRel = "emp_assigns", path = "emp_assigns")
public interface EmployeeAssignDataRest extends CrudRepository<EmployeeAssign, Long>{

	List<EmployeeAssign> findById(@Param("id") Long id);

	List<EmployeeAssign> findByEmployeeNumber(@Param("employeeNumber") String employeeNumber);

}
