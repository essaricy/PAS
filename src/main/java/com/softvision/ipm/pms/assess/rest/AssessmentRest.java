package com.softvision.ipm.pms.assess.rest;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.model.AssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;


@RestController
@RequestMapping(value="assessment", produces=MediaType.APPLICATION_JSON_VALUE)
public class AssessmentRest {

	private static final String SAVE_SELF_APPRAISAL = "SAVE_SELF_APPRAISAL";
	private static final String SUBMIT_SELF_APPRAISAL = "SUBMIT_SELF_APPRAISAL";
	private static final String AGREE_WITH_REVIEW = "AGREE_WITH_REVIEW";
	private static final String DISAGREE_WITH_REVIEW = "DISAGREE_WITH_REVIEW";

	private static final String REVERT_TO_SELF_SUBMISSION = "REVERT_TO_SELF_SUBMISSION";
	private static final String SAVE_MANAGER_REVIEW = "SAVE_MANAGER_REVIEW";
	private static final String SUBMIT_MANAGER_REVIEW = "SUBMIT_MANAGER_REVIEW";
	private static final String CONCLUDE = "CONCLUDE";

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@RequestMapping(value="list/phase/byAssignId/{aid}", method=RequestMethod.GET)
	public PhaseAssessmentDto getPhaseAssessmentByAssignmentId(
			@PathVariable(name = "aid", required = true) @NotNull long assignmentId)
			throws ServiceException {
		return phaseAssessmentService.getByAssignment(assignmentId, RestUtil.getLoggedInEmployeeId());
    }

	@RequestMapping(value="phase/save", method=RequestMethod.POST)
	public Result save(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SAVE_SELF_APPRAISAL);
    }

	@RequestMapping(value="phase/submit", method=RequestMethod.POST)
	public Result submit(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SUBMIT_SELF_APPRAISAL);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/revert", method=RequestMethod.POST)
	public Result askEmployeeToJustify(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, REVERT_TO_SELF_SUBMISSION);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/save-review", method=RequestMethod.POST)
	public Result saveReview(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SAVE_MANAGER_REVIEW);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/submit-review", method=RequestMethod.POST)
	public Result submitReview(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, SUBMIT_MANAGER_REVIEW);
	}

	@RequestMapping(value="phase/agree", method=RequestMethod.POST)
	public Result agree(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, AGREE_WITH_REVIEW);
	}

	@RequestMapping(value="phase/disagree", method=RequestMethod.POST)
	public Result disgree(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, DISAGREE_WITH_REVIEW);
	}

	@RequestMapping(value="phase/conclude", method=RequestMethod.POST)
    public Result conclude(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
        return changePhaseStatus(phaseAssessHeader, CONCLUDE);
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/update-review", method=RequestMethod.POST)
	public Result updateReview(@RequestBody(required = true) @NotNull AssessHeaderDto phaseAssessHeader) {
		Result result = new Result();
		try {
			if (phaseAssessHeader == null) {
				throw new ServiceException("Assessment information is missing");
			}
			long assignId = phaseAssessHeader.getAssignId();
			if (assignId == 0) {
				throw new ServiceException("Invalid assignment");
			}
			int requestedEmployeeId = RestUtil.getLoggedInEmployeeId();
			phaseAssessHeader.setAssessDate(new Date());
			phaseAssessmentService.updateReview(assignId, requestedEmployeeId, phaseAssessHeader);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

	public Result changePhaseStatus(AssessHeaderDto phaseAssessHeader, String actionToPerform) {
		Result result = new Result();
		try {
			if (phaseAssessHeader == null) {
				throw new ServiceException("Assessment information is missing");
			}
			long assignId = phaseAssessHeader.getAssignId();
			if (assignId == 0) {
				throw new ServiceException("Invalid assignment");
			}
			int requestedEmployeeId = RestUtil.getLoggedInEmployeeId();
			phaseAssessHeader.setAssessDate(new Date());

			if (actionToPerform.equals(SAVE_SELF_APPRAISAL)) {
				phaseAssessmentService.saveSelfAppraisal(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (actionToPerform.equals(SUBMIT_SELF_APPRAISAL)) {
				phaseAssessmentService.submitSelfAppraisal(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (actionToPerform.equals(REVERT_TO_SELF_SUBMISSION)) {
				phaseAssessmentService.revertToSelfSubmission(assignId, requestedEmployeeId);
			} else if (actionToPerform.equals(SAVE_MANAGER_REVIEW)) {
				phaseAssessmentService.saveReview(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (actionToPerform.equals(SUBMIT_MANAGER_REVIEW)) {
				phaseAssessmentService.submitReview(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (actionToPerform.equals(AGREE_WITH_REVIEW)) {
				phaseAssessmentService.agree(assignId, requestedEmployeeId);
			} else if (actionToPerform.equals(DISAGREE_WITH_REVIEW)) {
				phaseAssessmentService.escalate(assignId, requestedEmployeeId);
			} else if (actionToPerform.equals(CONCLUDE)) {
				phaseAssessmentService.conclude(assignId, requestedEmployeeId);
			} else {
				throw new Exception("Invalid action to perform: " + actionToPerform);
			}
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
	}

}
