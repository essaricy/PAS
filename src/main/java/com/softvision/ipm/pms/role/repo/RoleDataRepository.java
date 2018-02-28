package com.softvision.ipm.pms.role.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.role.entity.Role;

@Repository
public interface RoleDataRepository extends CrudRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

	List<Role> findById(int roleId);

	List<Role> findAll(Specification<Role> spec);

	@Query(value = "select r.id,r.role_name from employee_role e inner join role r on e.role_id=r.id  where e.employee_id=:employeeId", nativeQuery = true)
	List<Role> findByEmployeeId(@Param("employeeId") Long employeeId);

	@Query(value = "select e.* from employee e inner join employee_role r on e.EMPLOYEE_ID =r.EMPLOYEE_ID and r.role_id=:roleId", nativeQuery = true)
	List<Employee> findEmployeesbyRoleId(@Param("roleId") int roleId);

}
