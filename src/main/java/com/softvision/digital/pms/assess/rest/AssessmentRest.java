package com.softvision.digital.pms.assess.rest;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.assess.model.AssessHeaderDto;
import com.softvision.digital.pms.assess.model.PhaseAssessmentDto;
import com.softvision.digital.pms.assess.service.PhaseAssessmentService;
import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.RestUtil;


@RestController
@RequestMapping(value="api/assessment", produces=MediaType.APPLICATION_JSON_VALUE)
public class AssessmentRest {

	private static final String SAVE_SELF_APPRAISAL = "SAVE_SELF_APPRAISAL";
	private static final String SUBMIT_SELF_APPRAISAL = "SUBMIT_SELF_APPRAISAL";
	private static final String AGREE_WITH_REVIEW = "AGREE_WITH_REVIEW";
	private static final String DISAGREE_WITH_REVIEW = "DISAGREE_WITH_REVIEW";

	private static final String REVERT_TO_SELF_SUBMISSION = "REVERT_TO_SELF_SUBMISSION";
	private static final String SAVE_MANAGER_REVIEW = "SAVE_MANAGER_REVIEW";
	private static final String SUBMIT_MANAGER_REVIEW = "SUBMIT_MANAGER_REVIEW";
	private static final String UPDATE_MANAGER_REVIEW = "UPDATE_MANAGER_REVIEW";
	private static final String CONCLUDE = "CONCLUDE";

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE_OR_MANAGER)
	@GetMapping(value="list/phase/byAssignId/{aid}")
	public PhaseAssessmentDto getPhaseAssessmentByAssignmentId(
			@PathVariable(name = "aid", required = true) @NotNull long assignmentId)
			throws ServiceException {
		return phaseAssessmentService.getByAssignmentForEmployeeAndManager(assignmentId, RestUtil.getLoggedInEmployeeId(), null);
    }

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE)
	@PostMapping(value="phase/save")
	public Result save(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SAVE_SELF_APPRAISAL);
    }

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE)
	@PostMapping(value="phase/submit")
	public Result submit(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SUBMIT_SELF_APPRAISAL);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PostMapping(value="phase/revert")
	public Result askEmployeeToJustify(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, REVERT_TO_SELF_SUBMISSION);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PostMapping(value="phase/save-review")
	public Result saveReview(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SAVE_MANAGER_REVIEW);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PostMapping(value="phase/submit-review")
	public Result submitReview(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SUBMIT_MANAGER_REVIEW);
	}

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE)
	@PostMapping(value="phase/agree")
	public Result agree(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, AGREE_WITH_REVIEW);
	}

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE)
	@PostMapping(value="phase/disagree")
	public Result disgree(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, DISAGREE_WITH_REVIEW);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PostMapping(value="phase/conclude")
    public Result conclude(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
        return changePhaseStatus(phaseAssessHeader, CONCLUDE);
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@PostMapping(value="phase/update-review")
	public Result updateReview(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, UPDATE_MANAGER_REVIEW);
    }

	public Result changePhaseStatus(AssessHeaderDto assessHeader, String actionToPerform) {
		Result result = new Result();
		try {
			if (assessHeader == null) {
				throw new ServiceException("Assessment information is missing");
			}
			long assignId = assessHeader.getAssignId();
			if (assignId == 0) {
				throw new ServiceException("Invalid assignment");
			}
			int requestedEmployeeId = RestUtil.getLoggedInEmployeeId();
			if (actionToPerform.equals(SAVE_SELF_APPRAISAL)) {
				phaseAssessmentService.saveSelfAppraisal(assignId, requestedEmployeeId, assessHeader, null);
				result.setMessage("Your appraisal form has been saved successfully");
			} else if (actionToPerform.equals(SUBMIT_SELF_APPRAISAL)) {
				phaseAssessmentService.submitSelfAppraisal(assignId, requestedEmployeeId, assessHeader, null);
				result.setMessage("Your appraisal form has been submitted successfully");
			} else if (actionToPerform.equals(REVERT_TO_SELF_SUBMISSION)) {
				phaseAssessmentService.revertToSelfSubmission(assignId, requestedEmployeeId, null);
				result.setMessage("This appraisal form has been reverted back to employee successfully");
			} else if (actionToPerform.equals(SAVE_MANAGER_REVIEW)) {
				phaseAssessmentService.saveReview(assignId, requestedEmployeeId, assessHeader, null);
				result.setMessage("Your review has been saved successfully");
			} else if (actionToPerform.equals(SUBMIT_MANAGER_REVIEW)) {
				phaseAssessmentService.submitReview(assignId, requestedEmployeeId, assessHeader, null);
				result.setMessage("Your review has been submitted successfully");
			} else if (actionToPerform.equals(AGREE_WITH_REVIEW)) {
				phaseAssessmentService.agree(assignId, requestedEmployeeId, null);
				result.setMessage("Your appraisal form has been marked as AGREED and has been sent to your manager for conclusion");
			} else if (actionToPerform.equals(DISAGREE_WITH_REVIEW)) {
				phaseAssessmentService.disagree(assignId, requestedEmployeeId, null);
				result.setMessage("Your appraisal form has been marked as DISAGREED");
			} else if (actionToPerform.equals(UPDATE_MANAGER_REVIEW)) {
				phaseAssessmentService.updateReview(assignId, requestedEmployeeId, assessHeader, null);
				result.setMessage("Your review has been updated successfully");
			} else if (actionToPerform.equals(CONCLUDE)) {
				phaseAssessmentService.conclude(assignId, requestedEmployeeId, null);
				result.setMessage("This appraisal form has been CONCLUDED successfully");
			} else {
				throw new ValidationException("Invalid action to perform: " + actionToPerform);
			}
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
	}

}
