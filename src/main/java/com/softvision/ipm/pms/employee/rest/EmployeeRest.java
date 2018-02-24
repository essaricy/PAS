package com.softvision.ipm.pms.employee.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.service.EmployeeService;

@RestController
@RequestMapping(value="employee", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeRest {

	@Autowired private EmployeeService employeeService;

	@RequestMapping(value="/search/all", method=RequestMethod.GET)
    public @ResponseBody List<Employee> getEmployees() {
		return employeeService.getEmployees();
    }

	@RequestMapping(value="/search/byId/{employeeId}", method=RequestMethod.GET)
    public @ResponseBody Employee getEmployee(
    		@PathVariable(value="employeeId", required=true) Long employeeId) {
		return employeeService.getEmployee(employeeId);
    }

	@RequestMapping(value="/search/byLogin/{loginId}", method=RequestMethod.GET)
    public @ResponseBody Employee getEmployee(
    		@PathVariable(value="loginId", required=true) String loginId) {
		return employeeService.getEmployee(loginId);
    }

	@RequestMapping(value="/search/{searchString}", method=RequestMethod.GET)
    public @ResponseBody List<Employee> search(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return employeeService.search(searchString);
    }

	/*@RequestMapping(value="/save", method=RequestMethod.PUT)
    public @ResponseBody Employee saveEmployee(@RequestBody Employee employee) {
		System.out.println(employee);
		return employeeService.save(employee);
    }*/

	@RequestMapping(value="/save/byLogin/{loginId:.+}", method=RequestMethod.PUT)
    public @ResponseBody Result saveEmployee(
    		@PathVariable(value="loginId", required=true) String loginId) {
		Result result = new Result();
		try {
			System.out.println("loginId= " + loginId);
			Employee employee = employeeService.save(loginId);
			result.setCode(Result.SUCCESS);
			result.setContent(employee);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@RequestMapping(value="/types", method=RequestMethod.GET)
    public @ResponseBody List<String> getEmployeeTypes() {
		return employeeService.getEmployeeTypes();
    }

}
