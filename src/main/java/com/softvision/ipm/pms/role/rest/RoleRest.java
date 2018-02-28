package com.softvision.ipm.pms.role.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.service.RoleService;

@RestController
@RequestMapping(value = "role", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleRest {

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Role> getEmployees() {
		return roleService.getRoles();
	}

	@RequestMapping(value = "/byId/{roleId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getRolesByRole(@PathVariable(value = "roleId", required = true) int roleId) {
		return roleService.getRole(roleId);
	}

	@RequestMapping(value = "/byEmployee/{employeeId}", method = RequestMethod.GET)
	public @ResponseBody List<Role> getRolesByEmployee(
			@PathVariable(value = "employeeId", required = true) Long employeeId) {
		return roleService.getRolesbyEmployeeId(employeeId);
	}

	@RequestMapping(value = "/delete/{employeeId}/{roleId}", method = RequestMethod.DELETE)
	public Result delete(@PathVariable(required = true) @NotNull long employeeId,
			@PathVariable(required = true) @NotNull int roleId) {
		Result result = new Result();
		try {
			System.out.println("employeeId= " + employeeId);
			System.out.println("roleId= " + roleId);
			roleService.revokeRole(employeeId, roleId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
	}

	@RequestMapping(value = "/assign/{employeeId}/{roleId}", method = RequestMethod.PUT)
	public Result assign(@PathVariable(required = true) @NotNull long employeeId,
			@PathVariable(required = true) @NotNull int roleId) {
		Result result = new Result();
		try {
			System.out.println("employeeId= " + employeeId);
			System.out.println("roleId= " + roleId);
			roleService.assignRole(employeeId, roleId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
	}

}
