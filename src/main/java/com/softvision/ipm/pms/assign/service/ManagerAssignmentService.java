package com.softvision.ipm.pms.assign.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assign.constant.AssignmentPhaseStatus;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.repo.AssignmentPhaseDataRepository;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class ManagerAssignmentService {

	@Autowired private ManagerAssignmentRepository managerAssignmentRepository;

	@Autowired private AssignmentPhaseDataRepository assignmentPhaseDataRepository;

	//@Autowired private EmployeeRepository employeeRepository;

	@Autowired private RoleService roleService;

	public List<EmployeeAssignmentDto> getCurrentAssignments(int employeeId) {
		return managerAssignmentRepository.getCurrentPhases(employeeId);
	}

	public void changeManager(long phaseAssignId, int toEmployeeId)
			throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(phaseAssignId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		int status = employeePhaseAssignment.getStatus();
		AssignmentPhaseStatus assignmentPhaseStatus = AssignmentPhaseStatus.get(status);
		if (status > AssignmentPhaseStatus.SELF_APPRAISAL_PENDING.getCode()) {
			throw new ServiceException("Assignment is in '" + assignmentPhaseStatus.getName() + "' status. Cannot change the manager now");
		}
		if (!roleService.isManager(toEmployeeId)) {
			throw new ServiceException("The employee that you are trying to assign to, is not a manager");
		}
		// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changeManager(phaseAssignId, toEmployeeId);
		if (!changed) {
			throw new ServiceException("Unable to change assigned-by-manager");
		}
	}

	public void enableAppraisalFormToEmployee(long phaseAssignId) throws ServiceException {
		EmployeePhaseAssignment employeePhaseAssignment = assignmentPhaseDataRepository.findById(phaseAssignId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		int existingStatusId = employeePhaseAssignment.getStatus();
		AssignmentPhaseStatus existingStatus = AssignmentPhaseStatus.get(existingStatusId);
		//AssignmentPhaseStatus desiredStatus = AssignmentPhaseStatus.get(toStatusId);

		if (existingStatus != AssignmentPhaseStatus.ASSIGNED /*|| desiredStatus != AssignmentPhaseStatus.SELF_APPRAISAL_PENDING*/) {
			throw new ServiceException("Assignment status is not in ASSIGNED status.");
		}
		// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changeStatus(phaseAssignId, AssignmentPhaseStatus.SELF_APPRAISAL_PENDING.getCode());
		if (!changed) {
			throw new ServiceException("Unable to change assigned-by-manager");
		}
		// TODO email trigger
	}

}
