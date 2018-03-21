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

import com.softvision.ipm.pms.assess.model.CycleAssessmentDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.CycleAssessmentService;
import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;


@RestController
@RequestMapping(value="assessment", produces=MediaType.APPLICATION_JSON_VALUE)
public class AssessmentRest {

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@Autowired private CycleAssessmentService cycleAssessmentService;

	@RequestMapping(value="list/phase/byAssignId/{aid}", method=RequestMethod.GET)
	public PhaseAssessmentDto getPhaseAssessmentByAssignmentId(
			@PathVariable(name = "aid", required = true) @NotNull long assignmentId)
			throws ServiceException {
		return phaseAssessmentService.getByAssignment(assignmentId, RestUtil.getLoggedInEmployeeId());
    }

	@RequestMapping(value="list/cycle/byAssignId/{aid}", method=RequestMethod.GET)
	public CycleAssessmentDto getCycleAssessmentByAssignmentId(
			@PathVariable(name = "aid", required = true) @NotNull long assignmentId)
			throws ServiceException {
		return cycleAssessmentService.getByAssignment(assignmentId, RestUtil.getLoggedInEmployeeId());
    }

	@RequestMapping(value="phase/save", method=RequestMethod.POST)
	public Result save(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
    }

	@RequestMapping(value="phase/submit", method=RequestMethod.POST)
	public Result submit(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, PhaseAssignmentStatus.MANAGER_REVIEW_PENDING);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/save-review", method=RequestMethod.POST)
	public Result saveReview(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/submit-review", method=RequestMethod.POST)
	public Result submitReview(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED);
	}

	@RequestMapping(value="phase/agree", method=RequestMethod.POST)
	public Result agree(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, PhaseAssignmentStatus.EMPLOYEE_AGREED);
	}

	@RequestMapping(value="phase/disagree", method=RequestMethod.POST)
	public Result disgree(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
		return changePhaseStatus(phaseAssessHeader, PhaseAssignmentStatus.EMPLOYEE_ESCALATED);
	}

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/update-review", method=RequestMethod.POST)
	public Result updateReview(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessHeader) {
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

	public Result changePhaseStatus(PhaseAssessHeaderDto phaseAssessHeader, PhaseAssignmentStatus status) {
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
			if (status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
				phaseAssessmentService.saveSelfAppraisal(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
				phaseAssessmentService.submitSelfAppraisal(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
				phaseAssessmentService.saveReview(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED) {
				phaseAssessmentService.submitReview(assignId, requestedEmployeeId, phaseAssessHeader);
			} else if (status == PhaseAssignmentStatus.EMPLOYEE_AGREED) {
				phaseAssessmentService.agree(assignId, requestedEmployeeId);
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED) {
				phaseAssessmentService.escalate(assignId, requestedEmployeeId);
			}
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			
		}
		return result;
    }

}
