package com.softvision.ipm.pms.assess.rest;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.model.CycleAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.CycleAssessmentDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.CycleAssessmentService;
import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
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
	public Result savePhase(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessmentHeader) {
		return changePhaseStatus(phaseAssessmentHeader, PhaseAssignmentStatus.SELF_APPRAISAL_SAVED);
    }

	@RequestMapping(value="phase/submit", method=RequestMethod.POST)
	public Result submitPhase(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessmentHeader) {
		return changePhaseStatus(phaseAssessmentHeader, PhaseAssignmentStatus.MANAGER_REVIEW_PENDING);
	}

	@RequestMapping(value="phase/review", method=RequestMethod.POST)
	public Result reviewPhase(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessmentHeader) {
		return changePhaseStatus(phaseAssessmentHeader, PhaseAssignmentStatus.MANAGER_REVIEW_SAVED);
	}

	@RequestMapping(value="phase/conclude", method=RequestMethod.POST)
	public Result concludePhase(@RequestBody(required = true) @NotNull PhaseAssessHeaderDto phaseAssessmentHeader) {
		return changePhaseStatus(phaseAssessmentHeader, PhaseAssignmentStatus.CONCLUDED);
	}

	public Result changePhaseStatus(PhaseAssessHeaderDto phaseAssessmentHeader, PhaseAssignmentStatus status) {
		Result result = new Result();
		try {
			phaseAssessmentHeader.setAssessedBy(RestUtil.getLoggedInEmployeeId());
			phaseAssessmentHeader.setAssessDate(new Date());
			if (status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
				phaseAssessmentService.save(phaseAssessmentHeader);
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
				phaseAssessmentService.submit(phaseAssessmentHeader);
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
				phaseAssessmentService.review(phaseAssessmentHeader);
			} else if (status == PhaseAssignmentStatus.CONCLUDED) {
				phaseAssessmentService.reviewAndConclude(phaseAssessmentHeader);
			}
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@RequestMapping(value="cycle/review", method=RequestMethod.POST)
	public Result review(@RequestBody(required = true) @NotNull CycleAssessHeaderDto cycleAssessmentHeader) {
		Result result = new Result();
		try {
			cycleAssessmentHeader.setAssessedBy(RestUtil.getLoggedInEmployeeId());
			cycleAssessmentHeader.setAssessDate(new Date());
			cycleAssessmentService.review(cycleAssessmentHeader);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
	}

	@RequestMapping(value="cycle/conclude", method=RequestMethod.POST)
	public Result conclude(@RequestBody(required = true) @NotNull CycleAssessHeaderDto cycleAssessmentHeader) {
		Result result = new Result();
		try {
			cycleAssessmentHeader.setAssessedBy(RestUtil.getLoggedInEmployeeId());
			cycleAssessmentHeader.setAssessDate(new Date());
			cycleAssessmentService.reviewAndConclude(cycleAssessmentHeader);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
	}

}
