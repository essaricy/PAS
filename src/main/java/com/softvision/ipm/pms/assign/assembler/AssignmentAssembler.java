package com.softvision.ipm.pms.assign.assembler;

import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.employee.entity.Employee;

public class AssignmentAssembler {

	public static EmployeeAssignmentDto get(PhaseAssignment employeePhaseAssignment) {
		EmployeeAssignmentDto employeeAssignmentDto = null;
		if (employeePhaseAssignment != null) {
			employeeAssignmentDto = new EmployeeAssignmentDto();
			employeeAssignmentDto.setAssignedAt(employeePhaseAssignment.getAssignedAt());

			int assignedBy = employeePhaseAssignment.getAssignedBy();
			Employee assignedByEmp = new Employee();
			assignedByEmp.setEmployeeId(assignedBy);
			employeeAssignmentDto.setAssignedBy(assignedByEmp);

			int assignedTo = employeePhaseAssignment.getEmployeeId();
			Employee assignedToEmp = new Employee();
			assignedToEmp.setEmployeeId(assignedTo);
			employeeAssignmentDto.setAssignedTo(assignedToEmp);

			employeeAssignmentDto.setAssignmentId(employeePhaseAssignment.getId());
			employeeAssignmentDto.setStatus(employeePhaseAssignment.getStatus());
		}
		return employeeAssignmentDto;
	}

	public static EmployeeAssignmentDto get(CycleAssignment employeeCycleAssignment) {
		EmployeeAssignmentDto employeeAssignmentDto = null;
		if (employeeCycleAssignment != null) {
			employeeAssignmentDto = new EmployeeAssignmentDto();
			employeeAssignmentDto.setAssignedAt(employeeCycleAssignment.getAssignedAt());

			int assignedBy = employeeCycleAssignment.getAssignedBy();
			Employee assignedByEmp = new Employee();
			assignedByEmp.setEmployeeId(assignedBy);
			employeeAssignmentDto.setAssignedBy(assignedByEmp);

			int assignedTo = employeeCycleAssignment.getEmployeeId();
			Employee assignedToEmp = new Employee();
			assignedToEmp.setEmployeeId(assignedTo);
			employeeAssignmentDto.setAssignedTo(assignedToEmp);

			employeeAssignmentDto.setAssignmentId(employeeCycleAssignment.getId());
			employeeAssignmentDto.setStatus(employeeCycleAssignment.getStatus());
		}
		return employeeAssignmentDto;
	}

}
