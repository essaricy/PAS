package com.softvision.ipm.pms.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.util.CollectionUtil;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.service.EmployeeService;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.role.entity.EmployeeRole;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.role.repo.RoleRepository;

@Service
public class RoleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

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

	public int assignRole(Integer employeeId, int roleId) {
		LOGGER.info("assignRole(" + employeeId + ", " + roleId + ")");
		List<Role> roles=roleDataRepository.findByEmployeeId(employeeId);
		for (Role role : roles) {
			if (role.getId() == roleId) {
				throw new ValidationException("Manager role is already present, please check");
			}
		}
		int assign = roleRepository.assign(employeeId, roleId);
		LOGGER.info("assignRole(" + employeeId + ", " + roleId + ") completed. Result=" + assign);
		return assign;
	}

	public int removeRole(Integer employeeId, int roleId) {
		LOGGER.info("removeRole(" + employeeId + ", " + roleId + ")");
		int remove = roleRepository.remove(employeeId, roleId);
		LOGGER.info("removeRole(" + employeeId + ", " + roleId + ") completed. Result=" + remove);
		return remove;
	}
	
	public Map<Integer, Integer> changeManagerRole(EmployeeRole[] employeeList, int roleId) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int returnCode;
		boolean availableflag;
		for (EmployeeRole employeeRole : employeeList) {
			if (Boolean.parseBoolean(employeeRole.getManagerFlag())) {
				availableflag = true;
				List<Role> roles = roleDataRepository.findByEmployeeId(employeeRole.getId());
				for (Role role : roles) {
					if (role.getId() == roleId) {
						// Manager role is already present
						availableflag = false;
						break;
					}
				}
				returnCode = (availableflag) ? returnCode = roleRepository.assign(employeeRole.getId(), roleId) : 0;
			} else {
				returnCode = roleRepository.remove(employeeRole.getId(), roleId);
			}
			map.put(employeeRole.getId(), returnCode);
		}
		return map;
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
