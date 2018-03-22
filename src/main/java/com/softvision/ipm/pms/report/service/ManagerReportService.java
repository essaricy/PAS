package com.softvision.ipm.pms.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.report.model.PhasewiseEmployeeStatusCountDto;
import com.softvision.ipm.pms.report.repo.ManagerReportRepository;

@Service
public class ManagerReportService {

	@Autowired private ManagerReportRepository managerReportRepository;

	public List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCounts(int requestedEmplyeeId) {
		List<PhasewiseEmployeeStatusCountDto> phasewiseEmployeeStatusCounts = managerReportRepository.getPhasewiseEmployeeStatusCounts(requestedEmplyeeId);
		System.out.println("phasewiseEmployeeStatusCounts= " + phasewiseEmployeeStatusCounts);
		return phasewiseEmployeeStatusCounts;
	}

}
