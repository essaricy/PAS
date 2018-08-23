package com.softvision.digital.pms.role.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.role.entity.Role;

@Repository
public interface RoleDataRepository extends CrudRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

	List<Role> findAll();

	List<Role> findAll(Specification<Role> spec);

	List<Role> findById(int roleId);

	Role findByRoleName(String name);

	@Query(value = "select r.id,r.role_name from employee_role e inner join role r on e.role_id=r.id  where e.employee_id=:employeeId", nativeQuery = true)
	List<Role> findByEmployeeId(@Param("employeeId") Integer employeeId);

	@Query(value = "select e.* from employee e inner join employee_role r on e.EMPLOYEE_ID =r.EMPLOYEE_ID and r.role_id=:roleId", nativeQuery = true)
	List<Employee> findEmployeesByRoleId(@Param("roleId") int roleId);

}
