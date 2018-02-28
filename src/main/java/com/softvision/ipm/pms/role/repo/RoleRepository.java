package com.softvision.ipm.pms.role.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.role.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

	List<Role> findById(int roleId);

	List<Role> findAll(Specification<Role> spec);

//	List<Role> findByEmployeeId(Long EmployeeId);

//	List<Employee> findbyEmployeesbyRoleId(int roleId);

//	void update(Long employeeId, int roleId);

//	void revoke(Long employeeId, int roleId);

}
