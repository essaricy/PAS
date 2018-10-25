package com.softvision.digital.pms.role.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.employee.mapper.EmployeeMapper;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.employee.service.EmployeeService;
import com.softvision.digital.pms.role.constant.Roles;
import com.softvision.digital.pms.role.entity.EmployeeRole;
import com.softvision.digital.pms.role.entity.Role;
import com.softvision.digital.pms.role.repo.RoleDataRepository;
import com.softvision.digital.pms.role.repo.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleService {

	@Autowired private RoleDataRepository roleDataRepository;

	@Autowired private RoleRepository roleRepository;

	@Autowired private EmployeeService employeeService;

	@Autowired private EmployeeMapper employeeMapper;

	public List<Role> getRoles() {
		return roleDataRepository.findAll();
	}

	public List<Role> getRole(int roleId) {
		return roleDataRepository.findById(roleId);
	}

	public List<Role> getRolesbyEmployeeId(Integer employeeId) {
		return roleDataRepository.findByEmployeeId(employeeId);
	}

	public List<EmployeeDto> getEmployeesbyRoleId(int roleId) {
		return employeeMapper.getEmployeeDtoList(roleDataRepository.findEmployeesByRoleId(roleId));
	}

	public int assignRole(Integer employeeId, int roleId) {
	    log.info("assignRole: START employeeId={}, roleId={}", employeeId, roleId);
		List<Role> roles=roleDataRepository.findByEmployeeId(employeeId);
		for (Role role : roles) {
			if (role.getId() == roleId) {
				throw new ValidationException("Manager role is already present, please check");
			}
		}
		int recordsUpdated = roleRepository.assign(employeeId, roleId);
		log.info("assignRole: END employeeId={}, roleId={}, recordsUpdated=", employeeId, roleId, recordsUpdated);
		return recordsUpdated;
	}

	public int removeRole(Integer employeeId, int roleId) {
	    log.info("removeRole: START employeeId={}, roleId={}", employeeId, roleId);
		int recordsDeleted = roleRepository.remove(employeeId, roleId);
		log.info("removeRole: END employeeId={}, roleId={}, recordsDeleted=", employeeId, roleId, recordsDeleted);
		return recordsDeleted;
	}
	
	public Map<Integer, Integer> changeManagerRole(EmployeeRole[] employeeList, int roleId) {
	    log.info("changeManagerRole START");
		Map<Integer, Integer> map = new HashMap<>();
		int returnCode;
		for (EmployeeRole employeeRole : employeeList) {
		    log.info("changeManagerRole for " + employeeRole);
			if (Boolean.parseBoolean(employeeRole.getManagerFlag())) {
				List<Role> roles = roleDataRepository.findByEmployeeId(employeeRole.getId());
				boolean roleExists = roles.stream().anyMatch(o -> o.getId() == roleId);
				returnCode = (!roleExists) ? roleRepository.assign(employeeRole.getId(), roleId) : 0;
			} else {
				returnCode = roleRepository.remove(employeeRole.getId(), roleId);
			}
			map.put(employeeRole.getId(), returnCode);
		}
		log.info("changeManagerRole END");
		return map;
	}

	public List<Role> getRolesbyLoginId(String loginId) {
		EmployeeDto employee = employeeService.getEmployee(loginId);
		if (employee != null) {
			return roleDataRepository.findByEmployeeId(employee.getEmployeeId());
		}
		return new ArrayList<>();
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
			return roles.stream().anyMatch(o -> Roles.get(o.getRoleName()) == checkingRole);
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
		    return roles.stream().anyMatch(o -> Roles.get(o.getRoleName()) == checkingRole);
		}
		return false;
	}

}
