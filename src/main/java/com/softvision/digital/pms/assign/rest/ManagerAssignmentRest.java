package com.softvision.digital.pms.assign.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.assess.service.PhaseAssessmentService;
import com.softvision.digital.pms.assign.model.BulkAssignmentDto;
import com.softvision.digital.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.digital.pms.assign.service.AssignmentService;
import com.softvision.digital.pms.assign.service.ManagerAssignmentService;
import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.RestUtil;
import com.softvision.digital.pms.common.util.ResultUtil;

@RestController
@RequestMapping(value="api/assignment/manager", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerAssignmentRest {

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@Autowired private ManagerAssignmentService managerAssignmentService;

	@Autowired private AssignmentService assignmentService;

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value = "save/bulk", method = RequestMethod.POST)
	public Result saveBulk(@RequestBody(required = true) @NotNull BulkAssignmentDto bulkAssignmentDto) {
		Result saveResult = new Result();
		try {
			bulkAssignmentDto.setAssignedBy(RestUtil.getLoggedInEmployeeId());
			List<Result> results = assignmentService.bulkAssign(bulkAssignmentDto);
			saveResult.setCode(Result.SUCCESS);
			if (results != null && !results.isEmpty()) {
				long failedCount = results.stream().filter(o -> Result.FAILURE.equals(o.getCode())).count();
				if (failedCount == 0) {
					saveResult.setMessage("All asignments are successfully assigned for the selected employees");
				} else if (failedCount == results.size()) {
					saveResult.setMessage("All asignments are failed. Look for the individual status for reason");
				} else {
					saveResult.setMessage(
							"Some of the assignments are failed. Look for the individual status for reason");
				}
			}
			saveResult.setContent(results);
		} catch (Exception exception) {
			saveResult.setCode(Result.FAILURE);
			saveResult.setMessage(exception.getMessage());
		}
		return saveResult;
	}

	@GetMapping(value="list")
    public List<ManagerCycleAssignmentDto> getAllCycles() {
		return managerAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@GetMapping(value="active")
    public List<ManagerCycleAssignmentDto> getActiveCycles() {
        List<ManagerCycleAssignmentDto> cycleAssignmentDtos = new ArrayList<>();
        ManagerCycleAssignmentDto activeCycleAssignment = managerAssignmentService.getActiveCycle(RestUtil.getLoggedInEmployeeId());
        if (activeCycleAssignment != null) {
            cycleAssignmentDtos.add(activeCycleAssignment);
        }
        return cycleAssignmentDtos;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PutMapping(value="change/phase-assign/{phaseAssignId}/{toEmployeeId}")
	public Result assignToAnotherManager(
			@PathVariable(required=true) @Min(1) long phaseAssignId,
			@PathVariable(required=true) @Min(1) int toEmployeeId) {
		try {
			managerAssignmentService.changeManager(phaseAssignId, RestUtil.getLoggedInEmployeeId(), toEmployeeId);
			return ResultUtil.getSuccess("Assessing manager for this appraisal form has been changed successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PutMapping(value="reminder/tosubmit/{phaseAssignId}")
	public Result sendReminder(@PathVariable(required=true) @Min(1) long phaseAssignId) {
		try {
			managerAssignmentService.sendRemiderToSubmit(phaseAssignId, RestUtil.getLoggedInEmployeeId());
			return ResultUtil.getSuccess("A reminder has been sent successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PutMapping(value="change/phase-status/enable/{phaseAssignId}")
	public Result enablePhaseAppraisal(
			@PathVariable(required=true) @Min(1) long phaseAssignId) {
		try {
			phaseAssessmentService.enablePhaseAppraisal(phaseAssignId, RestUtil.getLoggedInEmployeeId(), null);
			return ResultUtil.getSuccess("Appraisal form has been enabled successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
    @PutMapping(value="change/phase-status/conclude/{phaseAssignId}")
    public Result concludePhaseAppraisal(
            @PathVariable(required=true) @Min(1) long phaseAssignId) {
        try {
            phaseAssessmentService.conclude(phaseAssignId, RestUtil.getLoggedInEmployeeId(), null);
            return ResultUtil.getSuccess("This appraisal form has been CONCLUDED successfully");
        } catch (Exception exception) {
        	return ResultUtil.getFailure(exception);
        }
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PutMapping(value="change/phase-status/revert/{phaseAssignId}")
	public Result revertToSelfSubmission(@PathVariable(required=true) @Min(1) long phaseAssignId) {
		try {
			phaseAssessmentService.revertToSelfSubmission(phaseAssignId, RestUtil.getLoggedInEmployeeId(), null);
            return ResultUtil.getSuccess("This appraisal form has been reverted back to employee successfully");
        } catch (Exception exception) {
        	return ResultUtil.getFailure(exception);
        }
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PutMapping(value="cycle/submit/{cycleAssignId}/{toEmployeeId}")
	public Result assignCycleToNextLevelManager(
			@PathVariable(name="cycleAssignId", required=true) @Min(1) long cycleAssignId,
			@PathVariable(name="toEmployeeId", required=true) @Min(1) int toEmployeeId) {
		try {
			managerAssignmentService.submitCycle(cycleAssignId, RestUtil.getLoggedInEmployeeId(), toEmployeeId);
			return ResultUtil.getSuccess("All the appraisal forms assigned in each phase for this appraisal cycle have been successfully submitted to the next level manager");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@DeleteMapping(value="delete/{assignmentId}")
	public Result deleteAssignment(@PathVariable(required=true) @Min(1) long assignmentId) {
		try {
			managerAssignmentService.deleteAssignment(assignmentId, RestUtil.getLoggedInEmployeeId());
			return ResultUtil.getSuccess("Appraisal form has been deleted successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

}
