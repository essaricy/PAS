package com.softvision.digital.pms.employee.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.ResultUtil;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.employee.service.EmployeeService;

@RestController
@RequestMapping(value="api/employee", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeRest {

	@Autowired private EmployeeService employeeService;

	@GetMapping(value="search/all")
    public @ResponseBody List<EmployeeDto> getEmployees() {
		return employeeService.getEmployees();
    }

	@GetMapping(value="search/byId/{employeeId}")
    public @ResponseBody EmployeeDto getEmployee(
    		@PathVariable(value="employeeId", required=true) int employeeId) {
		return employeeService.getEmployee(employeeId);
    }

	@GetMapping(value="search/byLogin/{loginId}")
    public @ResponseBody EmployeeDto getEmployee(
    		@PathVariable(value="loginId", required=true) String loginId) {
		return employeeService.getEmployee(loginId);
    }

	@GetMapping(value="search/{searchString}")
    public @ResponseBody List<EmployeeDto> search(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.search(searchString);
    }
	
	@GetMapping(value="role-search/{searchString}")
    public @ResponseBody List<EmployeeDto> roleSearch(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.roleSearch(searchString);
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PutMapping(value="sync")
    public @ResponseBody Result syncEmployees() {
		try {
			List<Result> syncEmployees = employeeService.syncEmployees();
			return ResultUtil.getSuccess("Employee SYNC has been completed successfully", syncEmployees);
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }
	

	@GetMapping(value="types")
    public @ResponseBody List<String> getEmployeeTypes() {
		return employeeService.getEmployeeTypes();
    }

}
