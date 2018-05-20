package com.softvision.ipm.pms.employee.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.employee.entity.Employee;

@Repository
public interface EmployeeDataRepository extends CrudRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

	@Query("select e from Employee e where active='Y'")
	List<Employee> findAll();

	List<Employee> findAll(Specification<Employee> spec);

	Employee findByEmployeeId(@Param("employeeId") Integer employeeId);

	Employee findByLoginIdIgnoreCase(@Param("loginId") String loginId);

	@Query("SELECT DISTINCT employmentType FROM Employee ORDER BY employmentType")
	List<String> findEmployeeTypes();
	
	@Query(value = "select e.* from employee e inner join employee_role er on e.EMPLOYEE_ID =er.EMPLOYEE_ID inner join role r on er.role_id=r.id and upper(r.role_name)=upper(:roleName) where active='Y' ", nativeQuery = true)
	List<Employee> findEmployeesByRoleName(@Param("roleName") String roleName);

}
