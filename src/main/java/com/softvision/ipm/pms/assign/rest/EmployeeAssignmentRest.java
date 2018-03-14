package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.EmployeeCycleAssignmentDto;
import com.softvision.ipm.pms.assign.service.EmployeeAssignmentService;
import com.softvision.ipm.pms.common.util.RestUtil;

@RestController
@RequestMapping(value="assignment/employee", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeAssignmentRest {

	@Autowired private EmployeeAssignmentService employeeAssignmentService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public List<EmployeeCycleAssignmentDto> getAllCycles() {
		return employeeAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

}
