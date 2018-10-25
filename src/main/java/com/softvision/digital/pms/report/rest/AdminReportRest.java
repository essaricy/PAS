package com.softvision.digital.pms.report.rest;

import java.util.List;

import javax.validation.constraints.Min;
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
import com.softvision.digital.pms.assign.model.AssignmentSummaryDto;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.report.model.PhaseAssignmentCountDto;
import com.softvision.digital.pms.report.service.AdminReportService;

@RestController
@RequestMapping(value = "api/admin/report", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminReportRest {

	@Autowired private AdminReportService adminReportService;

	@Autowired private PhaseAssessmentService phaseAssessmentService;

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@GetMapping(value = "cycle/{cycleId}")
	public List<EmployeeAssignmentDto> getAllAssignments(@PathVariable(required = true) @Min(1) int cycleId) {
		return adminReportService.getAllAssignments(cycleId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@GetMapping(value = "phase/{cycleId}/{phaseId}")
	public List<EmployeePhaseAssignmentDto> getAllAssignments(@PathVariable(required = true) @Min(1) int cycleId,
			@PathVariable(required = true) @Min(1) int phaseId) {
		return adminReportService.getAllAssignments(cycleId, phaseId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@GetMapping(value = "phase/status/count")
	public @ResponseBody List<PhaseAssignmentCountDto> getPhasewiseEmployeeStatusCount() {
		return adminReportService.getEmployeesCurrentAssignmentStatusCounts();
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@GetMapping(value = "phase/unassigned/{cycleId}/{phaseId}")
	public @ResponseBody List<EmployeeDto> getUnassignedEmployee(@PathVariable(required = true) @Min(1) int cycleId,
			@PathVariable(required = true) @Min(1) int phaseId) {
		return adminReportService.getUnassignedEmployee(cycleId, phaseId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@GetMapping(value = "phase/score/{cycleId}/{phaseId}")
	public @ResponseBody List<AssignmentSummaryDto> getAllEmployeesPhaseScore(
			@PathVariable(value = "cycleId", required = true) @Min(1) int cycleId,
			@PathVariable(value = "phaseId", required = true) @Min(1) int phaseId) {
		return adminReportService.getAllEmployeesPhaseScore(cycleId, phaseId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@GetMapping(value="phase/audit/byAssignId/{aid}")
	public PhaseAssessmentDto getPhaseAssessmentByAssignmentId(
			@PathVariable(name = "aid", required = true) @NotNull long assignmentId)
			throws ServiceException {
		return phaseAssessmentService.getAssignmentForAudit(assignmentId);
    }

}
