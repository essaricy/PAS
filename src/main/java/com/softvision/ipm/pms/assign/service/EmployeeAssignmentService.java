package com.softvision.ipm.pms.assign.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.repo.EmployeeAssignmentRepository;

@Service
public class EmployeeAssignmentService {

	@Autowired private EmployeeAssignmentRepository employeeAssignmentRepository;

	public List<EmployeeAssignmentDto> getCurrentAssignments(int employeeId) {
		return employeeAssignmentRepository.getCurrentPhases(employeeId);
	}

}
