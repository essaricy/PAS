package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.service.ManagerAssignmentService;
import com.softvision.ipm.pms.user.model.User;

@RestController
@RequestMapping(value="manager/assignment", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerAssignmentRest {

	@Autowired private ManagerAssignmentService managerAssignmentService;

	@RequestMapping(value="current", method=RequestMethod.GET)
    public List<EmployeeAssignmentDto> getCurrentAssignments() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getDetails();
		int employeeId = user.getEmployeeId();
		System.out.println("employeeId=" + employeeId);
		return managerAssignmentService.getCurrentAssignments(employeeId);
    }

}
