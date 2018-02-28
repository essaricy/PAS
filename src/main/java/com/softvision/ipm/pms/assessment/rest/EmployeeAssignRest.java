package com.softvision.ipm.pms.assessment.rest;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assessment.model.EmployeeAssignDto;
import com.softvision.ipm.pms.assessment.service.EmployeeAssignService;
import com.softvision.ipm.pms.common.model.Result;

@RestController
@RequestMapping(value="employeeAssign", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeAssignRest {

	@Autowired private EmployeeAssignService employeeAssignService;

	@RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody List<EmployeeAssignDto> geTemplates() {
		return employeeAssignService.getEmployeeAssigns();
    }

	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
    public @ResponseBody EmployeeAssignDto getTemplate(@PathVariable(required=true) @NotNull long id) {
		return null;
    }

	@RequestMapping(value="save", method=RequestMethod.POST)
    public Result save(@RequestBody(required=true) @NotNull EmployeeAssignDto employeeAssignDto) {
		Result result = new Result();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			employeeAssignDto.setAssignedBy(auth.getPrincipal().toString());	
			employeeAssignDto.setAssignedAt(new Date());
			
			//TODO  validation
			
			EmployeeAssignDto updated = employeeAssignService.update(employeeAssignDto);
			result.setCode(Result.SUCCESS);
			result.setContent(updated);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

}
