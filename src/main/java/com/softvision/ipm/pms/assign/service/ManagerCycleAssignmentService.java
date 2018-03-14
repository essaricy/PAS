package com.softvision.ipm.pms.assign.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class ManagerCycleAssignmentService {

	@Autowired private RoleService roleService;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Transactional
	public void assignCycleToNextManager(long cycleAssignId, int fromEmployeeId, int toEmployeeId)
			throws ServiceException {
		System.out.println("cycleAssignId= " + cycleAssignId);
		System.out.println("fromEmployeeId= " + fromEmployeeId);
		System.out.println("toEmployeeId= " + toEmployeeId);

		EmployeeCycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(cycleAssignId);
		if (employeeCycleAssignment == null) {
			throw new ServiceException("Assignment does not exist.");
		}
		// he must be the same employee who has been assigned
		int assignedBy = employeeCycleAssignment.getAssignedBy();
		if (assignedBy != fromEmployeeId) {
			throw new ServiceException("The manager who assessed the last phase can only assign it to the next manager");
		}
		int status = employeeCycleAssignment.getStatus();
		CycleAssignmentStatus cycleAssignmentStatus = CycleAssignmentStatus.get(status);
		if (status != CycleAssignmentStatus.ABRIDGED.getCode()) {
			throw new ServiceException("Assignment is in '" + cycleAssignmentStatus.getName() + "' status. Cannot change the manager now");
		}
		if (!roleService.isManager(toEmployeeId)) {
			throw new ServiceException("The employee that you are trying to assign to, is not a manager");
		}
		/*// Check if from and to employees are managers.
		boolean changed = managerAssignmentRepository.changeCycleManager(cycleAssignId, toEmployeeId);
		if (!changed) {
			throw new ServiceException("Unable to assign to the next manager");
		}*/
		employeeCycleAssignment.setAssignedBy(toEmployeeId);
		employeeCycleAssignment.setStatus(CycleAssignmentStatus.MANAGER_REVIEW_PENDING.getCode());
		EmployeeCycleAssignment saved = cycleAssignmentDataRepository.save(employeeCycleAssignment);
		System.out.println("saved=" + saved);
	}

}
