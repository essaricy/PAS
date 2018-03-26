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
public interface EmployeeRepository extends CrudRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

	Employee findByEmployeeId(Integer employeeId);

	Employee findByLoginId(String loginId);

	List<Employee> findAll(Specification<Employee> spec);

	@Query("SELECT DISTINCT employmentType FROM Employee ORDER BY employmentType")
	List<String> findEmployeeTypes();
	
	@Query(value = "select e.* from employee e inner join employee_role er on e.EMPLOYEE_ID =er.EMPLOYEE_ID inner join role r on er.role_id=r.id and upper(r.role_name)=upper(:roleName)", nativeQuery = true)
	List<Employee> findEmployeesByRoleName(@Param("roleName") String roleName);

}
