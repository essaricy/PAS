package com.softvision.ipm.pms.role.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.util.CollectionUtil;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public List<Role> getRoles() {
		return CollectionUtil.toList(roleRepository.findAll());
	}

	public List<Role> getRole(int roleId) {
		return roleRepository.findById(roleId);
	}

	public List<Role> getRolesbyEmployeeId(Long employeeId) {
//		return roleRepository.findByEmployeeId(employeeId);
		return null;
	}

	public List<Employee> getEmployeesbyRoleId(int roleId) {
//		return roleRepository.findbyEmployeesbyRoleId(roleId);
		return null;
	}
	
	public void assignRole(Long employeeId,int roleId){
//		 roleRepository.update(employeeId,roleId);
	}
	
	public void revokeRole(Long employeeId,int roleId){
//		roleRepository.revoke(employeeId,roleId);
	}

}
