package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.ipm.pms.assign.service.ManagerCycleAssignmentService;
import com.softvision.ipm.pms.assign.service.ManagerPhaseAssignmentService;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;

@RestController
@RequestMapping(value="assignment/manager", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerAssignmentRest {

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@Autowired private ManagerPhaseAssignmentService managerPhaseAssignmentService;

	@Autowired private ManagerCycleAssignmentService managerCycleAssignmentService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public List<ManagerCycleAssignmentDto> getAllCycles() {
		return managerPhaseAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@RequestMapping(value="change/phase-assign/{phaseAssignId}/{toEmployeeId}", method=RequestMethod.PUT)
	public Result assignPhaseToAnotherManager(
			@PathVariable(required=true) @Min(1) long phaseAssignId,
			@PathVariable(required=true) @Min(1) int toEmployeeId) {
		Result result = new Result();
		try {
			managerPhaseAssignmentService.assignPhaseToAnotherManager(phaseAssignId, RestUtil.getLoggedInEmployeeId(), toEmployeeId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@RequestMapping(value="change/phase-status/enable/{phaseAssignId}", method=RequestMethod.PUT)
	public Result enablePhaseAppraisal(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			managerPhaseAssignmentService.enablePhaseAppraisal(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

	@RequestMapping(value="change/phase-status/conclude/{phaseAssignId}", method=RequestMethod.PUT)
	public Result concludePhase(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		Result result = new Result();
		try {
			phaseAssessmentService.conclude(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@RequestMapping(value="change/cycle-assign/{cycleAssignId}/{toEmployeeId}", method=RequestMethod.PUT)
	public Result assignCycleToAnotherManager(
			@PathVariable(name="cycleAssignId", required=true) @Min(1) long cycleAssignId,
			@PathVariable(name="toEmployeeId", required=true) @Min(1) int toEmployeeId) {
		Result result = new Result();
		try {
			managerCycleAssignmentService.assignCycleToNextManager(cycleAssignId, RestUtil.getLoggedInEmployeeId(), toEmployeeId);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

}
