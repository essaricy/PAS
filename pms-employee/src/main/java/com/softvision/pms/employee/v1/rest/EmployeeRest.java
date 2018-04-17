package com.softvision.pms.employee.v1.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.pms.common.model.Result;
import com.softvision.pms.employee.v1.model.EmployeeDto;
import com.softvision.pms.employee.v1.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE, 
	value = "All operations pertaining to employee in PMS system")
public class EmployeeRest {

	@Autowired private EmployeeService employeeService;

	@ApiOperation(value = "List all the employees in the system", response = EmployeeDto.class, responseContainer="List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@RequestMapping(value="search/all", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeDto> getEmployees() {
		return employeeService.getEmployees();
    }

	@ApiOperation(value = "Search an employee by his/her Employee Id", response = EmployeeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved employee"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@RequestMapping(value="search/byId/{employeeId}", method=RequestMethod.GET)
    public @ResponseBody EmployeeDto getEmployee(
    		@PathVariable(value="employeeId", required=true) int employeeId) {
		return employeeService.getEmployee(employeeId);
    }

	@ApiOperation(value = "Search employee(s) by Employee Id, First Name or Last Name", response = EmployeeDto.class, responseContainer="List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@RequestMapping(value="search/{searchString}", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeDto> search(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.search(searchString);
    }
	
	@ApiOperation(value = "Search employee(s) by Role", response = EmployeeDto.class, responseContainer="List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@RequestMapping(value="role-search/{searchString}", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeDto> roleSearch(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.roleSearch(searchString);
    }

	@ApiOperation(value = "Sync all employees information from master system", response = Result.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully synced list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@RequestMapping(value="sync", method=RequestMethod.PATCH)
    public @ResponseBody Result syncEmployees() {
		Result result = new Result();
		try {
			List<Result> syncEmployees = employeeService.syncEmployees();
			result.setCode(Result.SUCCESS);
			result.setContent(syncEmployees);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }
	
}
