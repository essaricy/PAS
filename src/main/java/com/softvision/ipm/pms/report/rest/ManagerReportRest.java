package com.softvision.ipm.pms.report.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.ipm.pms.assign.service.ManagerAssignmentService;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.RestUtil;
import com.softvision.ipm.pms.report.model.PhasewiseEmployeeStatusCountDto;
import com.softvision.ipm.pms.report.service.ManagerReportService;

@RestController
@RequestMapping(value="api/manager/report", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerReportRest {

	@Autowired private ManagerReportService managerReportService;

	@Autowired private ManagerAssignmentService managerAssignmentService;

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/status/count", method=RequestMethod.GET)
    public @ResponseBody List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCount() {
		List<PhasewiseEmployeeStatusCountDto> phasewiseEmployeeStatusCounts = managerReportService
				.getPhasewiseEmployeeStatusCounts(RestUtil.getLoggedInEmployeeId());
		return phasewiseEmployeeStatusCounts;
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/score", method=RequestMethod.GET)
    public @ResponseBody List<ManagerCycleAssignmentDto> getPhaseScoreReport() {
		return managerAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="cycle/score", method=RequestMethod.GET)
    public @ResponseBody List<ManagerCycleAssignmentDto> getCyclesScoreReport() {
		return managerAssignmentService.getSubmittedCycles(RestUtil.getLoggedInEmployeeId());
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="cycle/score/{cycleId}/{employeeId}", method=RequestMethod.GET)
    public @ResponseBody ManagerCycleAssignmentDto getCycleScoreReport(
    		@PathVariable(value="cycleId", required = true) int cycleId,
    		@PathVariable(value="employeeId", required = true) int employeeId) {
		return managerAssignmentService.getSubmittedCycleByEmployeeId(cycleId, employeeId, RestUtil.getLoggedInEmployeeId());
    }

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE_OR_MANAGER)
	@RequestMapping(value="cycle/score/{cycleId}/{employeeId}/{assignId}", method=RequestMethod.GET)
	public PhaseAssessmentDto getCycleScoreReportByEmployeeByAssignId(
    		@PathVariable(value="cycleId", required = true) int cycleId,
    		@PathVariable(value="employeeId", required = true) int employeeId,
			@PathVariable(name = "assignId", required = true) @NotNull long assignId)
			throws ServiceException {
		return phaseAssessmentService.getByAssignmentForSubmittedToManager(cycleId, employeeId, assignId, RestUtil.getLoggedInEmployeeId());
    }

}
