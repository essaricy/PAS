package com.softvision.ipm.pms.assign.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.ipm.pms.assign.service.ManagerAssignmentService;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;

@RestController
@RequestMapping(value="assignment/manager", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerAssignmentRest {

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@Autowired private ManagerAssignmentService managerAssignmentService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public List<ManagerCycleAssignmentDto> getAllCycles() {
		return managerAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@RequestMapping(value="active", method=RequestMethod.GET)
    public List<ManagerCycleAssignmentDto> getActiveCycles() {
        List<ManagerCycleAssignmentDto> cycleAssignmentDtos = new ArrayList<>();
        ManagerCycleAssignmentDto activeCycleAssignment = managerAssignmentService.getActiveCycle(RestUtil.getLoggedInEmployeeId());
        if (activeCycleAssignment != null) {
            cycleAssignmentDtos.add(activeCycleAssignment);
        }
        return cycleAssignmentDtos;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="change/phase-assign/{phaseAssignId}/{toEmployeeId}", method=RequestMethod.PUT)
	public Result assignToAnotherManager(
			@PathVariable(required=true) @Min(1) long phaseAssignId,
			@PathVariable(required=true) @Min(1) int toEmployeeId) {
		Result result = new Result();
		try {
			managerAssignmentService.changeManager(phaseAssignId, RestUtil.getLoggedInEmployeeId(), toEmployeeId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="reminder/tosubmit/{phaseAssignId}", method=RequestMethod.PUT)
	public Result sendReminder(@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			managerAssignmentService.sendRemiderToSubmit(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="change/phase-status/enable/{phaseAssignId}", method=RequestMethod.PUT)
	public Result enablePhaseAppraisal(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.enablePhaseAppraisal(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
    @RequestMapping(value="change/phase-status/conclude/{phaseAssignId}", method=RequestMethod.PUT)
    public Result concludePhaseAppraisal(
            @PathVariable(required=true) @Min(1) long phaseAssignId) {
        Result result = new Result();
        try {
            phaseAssessmentService.conclude(phaseAssignId, RestUtil.getLoggedInEmployeeId());
            result.setCode(Result.SUCCESS);
        } catch (Exception exception) {
            result.setCode(Result.FAILURE);
            result.setMessage(exception.getMessage());
        }
        return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="change/phase-status/revert/{phaseAssignId}", method=RequestMethod.PUT)
	public Result revertToSelfSubmission(@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.revertToSelfSubmission(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="cycle/submit/{cycleAssignId}/{toEmployeeId}", method=RequestMethod.PUT)
	public Result assignCycleToNextLevelManager(
			@PathVariable(name="cycleAssignId", required=true) @Min(1) long cycleAssignId,
			@PathVariable(name="toEmployeeId", required=true) @Min(1) int toEmployeeId) {
		Result result = new Result();
		try {
			managerAssignmentService.submitCycle(
					cycleAssignId, RestUtil.getLoggedInEmployeeId(), toEmployeeId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="delete/{assignmentId}", method=RequestMethod.DELETE)
	public Result deleteAssignment(@PathVariable(required=true) @Min(1) long assignmentId) {
		Result result = new Result();
		try {
			managerAssignmentService.deleteAssignment(assignmentId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
    }

}
