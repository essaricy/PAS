package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.CycleAssignmentDto;
import com.softvision.ipm.pms.assign.service.ManagerAssignmentService;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.user.model.User;

@RestController
@RequestMapping(value="assignment/manager", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerAssignmentRest {

	@Autowired private ManagerAssignmentService managerAssignmentService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public List<CycleAssignmentDto> getAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getDetails();
		int employeeId = user.getEmployeeId();
		System.out.println("employeeId=" + employeeId);
		return managerAssignmentService.getAll(employeeId);
    }

	@RequestMapping(value="change/assignedBy/{phaseAssignId}/{toEmployeeId}", method=RequestMethod.PUT)
	public Result changeAssignedBy(
			@PathVariable(required=true) @Min(1) long phaseAssignId,
			@PathVariable(required=true) @Min(1) int toEmployeeId) {
		Result result = new Result();
		try {
			managerAssignmentService.changeManager(phaseAssignId, toEmployeeId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@RequestMapping(value="change/phase-status/enable/{phaseAssignId}", method=RequestMethod.PUT)
	public Result enablePhaseFormToEmployee(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			managerAssignmentService.enableAppraisalFormToEmployee(phaseAssignId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

}
