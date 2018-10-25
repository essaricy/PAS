package com.softvision.digital.pms.report.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.assign.model.PhaseAssignmentDto;
import com.softvision.digital.pms.common.util.RestUtil;
import com.softvision.digital.pms.report.service.EmployeeReportService;

@RestController
@RequestMapping(value="api/employee/report", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeReportRest {

	@Autowired private EmployeeReportService employeeReportService;

	@GetMapping(value="cycle/status")
    public @ResponseBody List<PhaseAssignmentDto> getPhasewiseEmployeeStatusCount() {
		return employeeReportService.getCurrentPhaseAssignment(RestUtil.getLoggedInEmployeeId());
    }

}
