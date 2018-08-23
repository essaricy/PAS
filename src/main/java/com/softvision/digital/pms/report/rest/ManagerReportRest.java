package com.softvision.digital.pms.report.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.assess.model.PhaseAssessmentDto;
import com.softvision.digital.pms.assess.service.PhaseAssessmentService;
import com.softvision.digital.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.digital.pms.assign.service.ManagerAssignmentService;
import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.util.RestUtil;
import com.softvision.digital.pms.report.model.PhasewiseEmployeeStatusCountDto;
import com.softvision.digital.pms.report.service.ManagerReportService;

@RestController
@RequestMapping(value="api/manager/report", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerReportRest {

	@Autowired private ManagerReportService managerReportService;

	@Autowired private ManagerAssignmentService managerAssignmentService;

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@GetMapping(value="phase/status/count")
    public @ResponseBody List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCount() {
		return managerReportService.getPhasewiseEmployeeStatusCounts(RestUtil.getLoggedInEmployeeId());
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@GetMapping(value="phase/score")
    public @ResponseBody List<ManagerCycleAssignmentDto> getPhaseScoreReport() {
		return managerAssignmentService.getAllCycles(RestUtil.getLoggedInEmployeeId());
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@GetMapping(value="cycle/score")
    public @ResponseBody List<ManagerCycleAssignmentDto> getCyclesScoreReport() {
		return managerAssignmentService.getSubmittedCycles(RestUtil.getLoggedInEmployeeId());
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@GetMapping(value="cycle/score/{cycleId}/{employeeId}")
    public @ResponseBody ManagerCycleAssignmentDto getCycleScoreReport(
    		@PathVariable(value="cycleId", required = true) int cycleId,
    		@PathVariable(value="employeeId", required = true) int employeeId) throws ServiceException {
		return managerAssignmentService.getSubmittedCycleByEmployeeId(cycleId, employeeId);
    }

	@PreAuthorize(AuthorizeConstant.IS_EMPLOYEE_OR_MANAGER)
	@GetMapping(value="cycle/score/{cycleId}/{employeeId}/{assignId}")
	public PhaseAssessmentDto getCycleScoreReportByEmployeeByAssignId(
    		@PathVariable(value="cycleId", required = true) int cycleId,
    		@PathVariable(value="employeeId", required = true) int employeeId,
			@PathVariable(name = "assignId", required = true) @NotNull long assignId)
			throws ServiceException {
		return phaseAssessmentService.getByAssignmentForSubmittedToManager(cycleId, employeeId, assignId, RestUtil.getLoggedInEmployeeId());
    }

}
