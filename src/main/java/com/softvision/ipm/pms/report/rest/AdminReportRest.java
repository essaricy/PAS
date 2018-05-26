package com.softvision.ipm.pms.report.rest;

import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.report.model.PhaseAssignmentCountDto;
import com.softvision.ipm.pms.report.service.AdminReportService;

@RestController
@RequestMapping(value = "api/admin/report", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminReportRest {

	@Autowired
	private AdminReportService adminReportService;

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "cycle/{cycleId}", method = RequestMethod.GET)
	public List<EmployeeAssignmentDto> getAllAssignments(@PathVariable(required = true) @Min(1) int cycleId) {
		return adminReportService.getAllAssignments(cycleId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "phase/{cycleId}/{phaseId}", method = RequestMethod.GET)
	public List<EmployeePhaseAssignmentDto> getAllAssignments(@PathVariable(required = true) @Min(1) int cycleId,
			@PathVariable(required = true) @Min(1) int phaseId) {
		return adminReportService.getAllAssignments(cycleId, phaseId);
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "phase/status/count", method = RequestMethod.GET)
	public @ResponseBody List<PhaseAssignmentCountDto> getPhasewiseEmployeeStatusCount() {
		List<PhaseAssignmentCountDto> phasewiseEmployeeStatusCounts = adminReportService
				.getEmployeesCurrentAssignmentStatusCounts();
		return phasewiseEmployeeStatusCounts;
	}

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value = "phase/unassigned/{cycleId}/{phaseId}", method = RequestMethod.GET)
	public @ResponseBody List<Employee> getUnassignedEmployee(@PathVariable(required = true) @Min(1) int cycleId,
			@PathVariable(required = true) @Min(1) int phaseId) {
		return adminReportService.getUnassignedEmployee(cycleId, phaseId);
	}

}
