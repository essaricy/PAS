package com.softvision.ipm.pms.report.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.PhaseAssignmentDto;
import com.softvision.ipm.pms.common.util.RestUtil;
import com.softvision.ipm.pms.report.service.EmployeeReportService;

@RestController
@RequestMapping(value="api/employee/report", produces=MediaType.APPLICATION_JSON_VALUE)
public class EmployeeReportRest {

	@Autowired private EmployeeReportService employeeReportService;

	@RequestMapping(value="cycle/status", method=RequestMethod.GET)
    public @ResponseBody List<PhaseAssignmentDto> getPhasewiseEmployeeStatusCount() {
		List<PhaseAssignmentDto> phaseAssignments = employeeReportService
				.getCurrentPhaseAssignment(RestUtil.getLoggedInEmployeeId());
		return phaseAssignments;
    }

}
