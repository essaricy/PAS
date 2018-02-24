package com.softvision.ipm.pms.assessment.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.softvision.ipm.pms.assessment.model.EmployeeAssess;

@RepositoryRestResource(collectionResourceRel = "emp_assesses", path = "emp_assesses")
public interface EmployeeAssessDataRest extends CrudRepository<EmployeeAssess, Long>{

	List<EmployeeAssess> findById(@Param("id") Long id);

}
