package com.softvision.ipm.pms.assign.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.rest.ManagerAssignmentRepository;

@Service
public class ManagerAssignmentService {

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	public List<EmployeeAssignmentDto> getCurrentAssignments(int employeeId) {
		return managerAssignmentRepository.getCurrentPhases(employeeId);
	}

}
