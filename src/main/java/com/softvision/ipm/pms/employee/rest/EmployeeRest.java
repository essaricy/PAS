package com.softvision.ipm.pms.employee.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.ResultUtil;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.employee.service.EmployeeService;

@RestController
@RequestMapping(value="api/employee", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeRest {

	@Autowired private EmployeeService employeeService;

	@RequestMapping(value="search/all", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeDto> getEmployees() {
		return employeeService.getEmployees();
    }

	@RequestMapping(value="search/byId/{employeeId}", method=RequestMethod.GET)
    public @ResponseBody EmployeeDto getEmployee(
    		@PathVariable(value="employeeId", required=true) int employeeId) {
		return employeeService.getEmployee(employeeId);
    }

	@RequestMapping(value="search/byLogin/{loginId}", method=RequestMethod.GET)
    public @ResponseBody EmployeeDto getEmployee(
    		@PathVariable(value="loginId", required=true) String loginId) {
		return employeeService.getEmployee(loginId);
    }

	@RequestMapping(value="search/{searchString}", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeDto> search(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.search(searchString);
    }
	
	@RequestMapping(value="role-search/{searchString}", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeDto> roleSearch(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.roleSearch(searchString);
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="sync", method=RequestMethod.PUT)
    public @ResponseBody Result syncEmployees() {
		try {
			List<Result> syncEmployees = employeeService.syncEmployees();
			return ResultUtil.getSuccess("Employee SYNC has been completed successfully", syncEmployees);
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }
	

	@RequestMapping(value="types", method=RequestMethod.GET)
    public @ResponseBody List<String> getEmployeeTypes() {
		return employeeService.getEmployeeTypes();
    }

}
