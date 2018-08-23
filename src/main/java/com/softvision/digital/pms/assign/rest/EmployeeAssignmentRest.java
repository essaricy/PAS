package com.softvision.digital.pms.assign.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.assess.service.PhaseAssessmentService;
import com.softvision.digital.pms.assign.model.EmployeeCycleAssignmentDto;
import com.softvision.digital.pms.assign.service.EmployeeAssignmentService;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.RestUtil;

@RestController
@RequestMapping(value="api/assignment/employee", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeAssignmentRest {

	@Autowired private EmployeeAssignmentService employeeAssignmentService;

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@GetMapping(value="list")
    public List<EmployeeCycleAssignmentDto> getAllCycles() {
		return employeeAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@GetMapping(value="active")
    public List<EmployeeCycleAssignmentDto> getActiveCycle() {
		List<EmployeeCycleAssignmentDto> cycleAssignmentDtos = new ArrayList<>();
		EmployeeCycleAssignmentDto activeCycleAssignment = employeeAssignmentService.getActiveCycle(RestUtil.getLoggedInEmployeeId());
		if (activeCycleAssignment != null) {
			cycleAssignmentDtos.add(activeCycleAssignment);
		}
		return cycleAssignmentDtos;
    }

	@PutMapping(value="change/phase-status/agree/{phaseAssignId}")
	public Result agreeReview(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.agree(phaseAssignId, RestUtil.getLoggedInEmployeeId(), null);
			result.setMessage("Your appraisal form has been marked as AGREED and has been sent to your manager for conclusion");
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

	@PutMapping(value="change/phase-status/escalate/{phaseAssignId}")
	public Result escalate(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.disagree(phaseAssignId, RestUtil.getLoggedInEmployeeId(), null);
			result.setMessage("Your appraisal form has been marked as DISAGREED");
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

}
