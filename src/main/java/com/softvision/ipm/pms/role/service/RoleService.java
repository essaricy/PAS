package com.softvision.ipm.pms.role.service;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.constants.Roles;
import com.softvision.ipm.pms.common.util.CollectionUtil;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.service.EmployeeService;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.role.repo.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleDataRepository roleDataRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeService employeeService;

	public List<Role> getRoles() {
		return CollectionUtil.toList(roleDataRepository.findAll());
	}

	public List<Role> getRole(int roleId) {
		return roleDataRepository.findById(roleId);
	}

	public List<Role> getRolesbyEmployeeId(Integer employeeId) {
		return roleDataRepository.findByEmployeeId(employeeId);
	}

	public List<Employee> getEmployeesbyRoleId(int roleId) {
		return roleDataRepository.findEmployeesByRoleId(roleId);
	}

	public int assignRole(Long employeeId, int roleId) {
		List<Role> roles=roleDataRepository.findByEmployeeId(employeeId);
		for (Role role : roles) {
			if(role.getId()==roleId){
				throw new ValidationException("Manager role is already present, please check");
			}
		}
		return roleRepository.assign(employeeId, roleId);
	}

	public int removeRole(Long employeeId, int roleId) {
		return roleRepository.remove(employeeId, roleId);
	}

	public List<Role> getRolesbyLoginId(String loginId) {
		Employee employee = employeeService.getEmployee(loginId);
		if (employee != null) {
			return roleDataRepository.findByEmployeeId(employee.getEmployeeId());
		}
		return null;
	}

	public boolean isAdmin(int employeeId) {
		return isInRole(employeeId, Roles.ADMIN);
	}

	public boolean isManager(int employeeId) {
		return isInRole(employeeId, Roles.MANAGER);
	}

	private boolean isInRole(int employeeId, Roles checkingRole) {
		List<Role> roles = getRolesbyEmployeeId(employeeId);
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				Roles currentRole = Roles.get(role.getRoleName());
				if (currentRole == checkingRole) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAdmin(String loginId) {
		return isInRole(loginId, Roles.ADMIN);
	}

	public boolean isManager(String loginId) {
		return isInRole(loginId, Roles.MANAGER);
	}

	private boolean isInRole(String loginId, Roles checkingRole) {
		List<Role> roles = getRolesbyLoginId(loginId);
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				Roles currentRole = Roles.get(role.getRoleName());
				if (currentRole == checkingRole) {
					return true;
				}
			}
		}
		return false;
	}

}
