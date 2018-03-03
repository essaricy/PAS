package com.softvision.ipm.pms.role.repo;

import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class RoleRepository extends AbstractRepository {

	public int assign(Integer employeeId, int roleId) {
		return jdbcTemplate.update(
				"INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), ?,?)",
				 roleId,employeeId);
	}

	public int remove(Integer employeeId, int roleId) {
		return jdbcTemplate.update("delete from employee_role where employee_id=? and role_id=?", employeeId, roleId);
	}

}
