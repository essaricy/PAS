package com.softvision.ipm.pms.assign.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.model.EmployeeCycleAssignmentDto;
import com.softvision.ipm.pms.assign.service.EmployeeAssignmentService;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;

@RestController
@RequestMapping(value="assignment/employee", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeAssignmentRest {

	@Autowired private EmployeeAssignmentService employeeAssignmentService;

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public List<EmployeeCycleAssignmentDto> getAllCycles() {
		return employeeAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@RequestMapping(value="active", method=RequestMethod.GET)
    public List<EmployeeCycleAssignmentDto> getActiveCycle() {
		List<EmployeeCycleAssignmentDto> cycleAssignmentDtos = new ArrayList<>();
		EmployeeCycleAssignmentDto activeCycleAssignment = employeeAssignmentService.getActiveCycle(RestUtil.getLoggedInEmployeeId());
		if (activeCycleAssignment != null) {
			cycleAssignmentDtos.add(activeCycleAssignment);
		}
		return cycleAssignmentDtos;
    }

	@RequestMapping(value="change/phase-status/agree/{phaseAssignId}", method=RequestMethod.PUT)
	public Result agreeReview(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.agree(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

	@RequestMapping(value="change/phase-status/escalate/{phaseAssignId}", method=RequestMethod.PUT)
	public Result escalate(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.escalate(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

}
