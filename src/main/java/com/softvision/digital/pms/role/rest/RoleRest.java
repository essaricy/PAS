package com.softvision.digital.pms.role.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.ResultUtil;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.role.constant.Roles;
import com.softvision.digital.pms.role.entity.EmployeeRole;
import com.softvision.digital.pms.role.entity.Role;
import com.softvision.digital.pms.role.service.RoleService;

@RestController
@RequestMapping(value = "api/role", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleRest {

	@Autowired private RoleService roleService;

	@GetMapping(value = "all")
	public @ResponseBody List<Role> getRoles() {
		return roleService.getRoles();
	}

	@GetMapping(value = "byId/{roleId}")
	public @ResponseBody List<Role> getRolesByRole(@PathVariable(value = "roleId", required = true) int roleId) {
		return roleService.getRole(roleId);
	}

	@GetMapping(value = "emp-byRole/{roleId}")
	public @ResponseBody List<EmployeeDto> getEmployeeByRole(@PathVariable(value = "roleId", required = true) int roleId) {
		return roleService.getEmployeesbyRoleId(roleId);
	}
	@GetMapping(value = "byEmployee/{empId}")
	public @ResponseBody List<Role> getRolesByEmp(@PathVariable(value = "empId", required = true) int employeeId) {
		return roleService.getRolesbyEmployeeId(employeeId);
	}
	@GetMapping(value = "byLogin/{loginId}")
	public @ResponseBody List<Role> getRolesBylogin(@PathVariable(value = "loginId", required = true) String loginId) {
		return roleService.getRolesbyLoginId(loginId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PutMapping(value = "delete/{employeeId}/{roleId}")
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
	@PutMapping(value = "assign/{employeeId}/{roleId}")
	public Result assign(@PathVariable(required = true) @NotNull Integer employeeId,
			@PathVariable(required = true) @NotNull int roleId) {
		try {
			roleService.assignRole(employeeId, roleId);
			return ResultUtil.getSuccess("Roles have been assigned successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
	}
	
	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PostMapping(value = "changeManagerRole")
	public Result changeManagerRole(@RequestBody(required = true) @NotNull EmployeeRole[] employeeList) {
		try {
			roleService.changeManagerRole(employeeList, Roles.MANAGER.getCode());
			return ResultUtil.getSuccess("Roles have been assigned successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
	}

}
