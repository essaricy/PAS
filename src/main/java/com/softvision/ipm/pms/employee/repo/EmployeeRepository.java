package com.softvision.ipm.pms.employee.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.employee.entity.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Employee findByEmployeeId(Long employeeId);

	Employee findByLoginId(String loginId);

	List<Employee> findAll(Specification<Employee> spec);

	@Query("SELECT DISTINCT employmentType FROM Employee ORDER BY employmentType")
	List<String> findEmployeeTypes();

}
