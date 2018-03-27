package com.softvision.ipm.pms.role.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.role.entity.EmployeeRole;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.service.RoleService;

@RestController
@RequestMapping(value = "role", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleRest {

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public @ResponseBody List<Role> getRoles() {
		return roleService.getRoles();
	}

	@RequestMapping(value = "byId/{roleId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getRolesByRole(@PathVariable(value = "roleId", required = true) int roleId) {
		return roleService.getRole(roleId);
	}

	@RequestMapping(value = "emp-byRole/{roleId}", method = RequestMethod.GET)
	public @ResponseBody List<Employee> getEmployeeByRole(@PathVariable(value = "roleId", required = true) int roleId) {
		return roleService.getEmployeesbyRoleId(roleId);
	}
	@RequestMapping(value = "byEmployee/{empId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getRolesByEmp(@PathVariable(value = "empId", required = true) int employeeId) {
		return roleService.getRolesbyEmployeeId(employeeId);
	}
	@RequestMapping(value = "byLogin/{loginId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getRolesBylogin(@PathVariable(value = "loginId", required = true) String loginId) {
		return roleService.getRolesbyLoginId(loginId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "delete/{employeeId}/{roleId}", method = RequestMethod.PUT)
	public Result delete(@PathVariable(required = true) @NotNull Integer employeeId,
			@PathVariable(required = true) @NotNull int roleId) {
		Result result = new Result();
		try {
			roleService.removeRole(employeeId, roleId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "assign/{employeeId}/{roleId}", method = RequestMethod.PUT)
	public Result assign(@PathVariable(required = true) @NotNull Integer employeeId,
			@PathVariable(required = true) @NotNull int roleId) {
		Result result = new Result();
		try {
			roleService.assignRole(employeeId, roleId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
	}
	
	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "changeManagerRole", method = RequestMethod.POST)
	public Result changeManagerRole(@RequestBody(required = true) @NotNull EmployeeRole[] employeeList) {
		Result result = new Result();
		try {
			roleService.changeManagerRole(employeeList, Roles.MANAGER_ROLE_ID);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
	}

}
