package com.softvision.ipm.pms.report.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.util.RestUtil;
import com.softvision.ipm.pms.report.model.PhasewiseEmployeeStatusCountDto;
import com.softvision.ipm.pms.report.service.ManagerReportService;

@RestController
@RequestMapping(value="report/manager", produces=MediaType.APPLICATION_JSON_VALUE)
public class ManagerReportRest {

	@Autowired private ManagerReportService managerReportService;

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="phase/status/count", method=RequestMethod.GET)
    public @ResponseBody List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCount() {
		List<PhasewiseEmployeeStatusCountDto> phasewiseEmployeeStatusCounts = managerReportService
				.getPhasewiseEmployeeStatusCounts(RestUtil.getLoggedInEmployeeId());
		return phasewiseEmployeeStatusCounts;
    }

}
