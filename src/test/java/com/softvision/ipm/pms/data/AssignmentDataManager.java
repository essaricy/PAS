package com.softvision.ipm.pms.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.digital.pms.appraisal.service.AppraisalService;
import com.softvision.digital.pms.assign.model.BulkAssignmentDto;
import com.softvision.digital.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.digital.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.digital.pms.assign.service.AssignmentService;
import com.softvision.digital.pms.assign.service.ManagerAssignmentService;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.employee.service.EmployeeService;
import com.softvision.digital.pms.role.constant.Roles;
import com.softvision.digital.pms.template.model.TemplateDto;
import com.softvision.digital.pms.template.service.TemplateService;

@Component
public class AssignmentDataManager implements AbstractDataManager {

	@Autowired AssignmentService assignmentService;

	@Autowired ManagerAssignmentService managerAssignmentService;

	@Autowired AppraisalService appraisalService;

	@Autowired TemplateService templateService;

	@Autowired EmployeeService employeeService;

	@Autowired CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Override
	public void clearData() throws Exception {
		clearManagerAssignments();
	}

	private void clearManagerAssignments() {
		/*List<Employee> managers = roleService.getEmployeesbyRoleId(Roles.MANAGER.getCode());
		for (Employee manager : managers) {
			int employeeId = manager.getEmployeeId();
			List<ManagerCycleAssignmentDto> allCycles = managerAssignmentService.getAllCycles(employeeId);
			if (allCycles != null && !allCycles.isEmpty()) {
				for (ManagerCycleAssignmentDto managerCycleAssignment : allCycles) {
					List<EmployeeAssignmentDto> cycleAssignments = managerCycleAssignment.getEmployeeAssignments();
					for (EmployeeAssignmentDto cycleAssignment : cycleAssignments) {
						long assignmentId = cycleAssignment.getAssignmentId();
						cycleAssignmentDataRepository.delete(assignmentId);
					}
					List<PhaseAssignmentDto> phaseAssignments = managerCycleAssignment.getPhaseAssignments();
					if (phaseAssignments != null && !phaseAssignments.isEmpty()) {
						for (PhaseAssignmentDto phaseAssignmentDto : phaseAssignments) {
							List<EmployeeAssignmentDto> employeeAssignments2 = phaseAssignmentDto.getEmployeeAssignments();
							if (employeeAssignments2 != null && !employeeAssignments2.isEmpty()) {
								for (EmployeeAssignmentDto employeeAssignmentDto : employeeAssignments2) {
									phaseAssignmentDataRepository.delete(employeeAssignmentDto.getAssignmentId());
								}
							}
						}
					}
				}
			}
		}*/
	}

	@Override
	public void loadData() throws Exception {
		List<TemplateDto> templates = templateService.getTemplates();

		int numberOfManagers=10;
		int numberOfEmployees=5;

		List<Integer> usedManagers = new ArrayList<>();
		List<Integer> usedEmployees = new ArrayList<>();
		AppraisalCycleDto activeCycle = appraisalService.getActiveCycle();
		List<EmployeeDto> managers = employeeService.roleSearch(Roles.MANAGER.getName());
		List<EmployeeDto> employees = employeeService.getEmployees();

		for (int managerIndex = 0; managerIndex < numberOfManagers; managerIndex++) {
			EmployeeDto randomManager = managers.get(RANDOM.nextInt(managers.size()));
			int managerId = randomManager.getEmployeeId();
			if (usedManagers.contains(managerId)) {
				managerIndex--;
				continue;
			}
			usedManagers.add(managerId);

			TemplateDto randomTemplate = templates.get(RANDOM.nextInt(templates.size()));

			BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
			bulkAssignmentDto.setAssignedBy(managerId);
			bulkAssignmentDto.setCycleId(activeCycle.getId());
			bulkAssignmentDto.setPhaseId(activeCycle.getPhases().get(0).getId());
			bulkAssignmentDto.setTemplateId(randomTemplate.getId());

			List<Integer> employeeIds = new ArrayList<>();
			for (int employeeIndex = 0; employeeIndex < numberOfEmployees; employeeIndex++) {
				EmployeeDto randomEmployee = employees.get(RANDOM.nextInt(employees.size()));
				int employeeId = randomEmployee.getEmployeeId();
				if (usedEmployees.contains(employeeId)) {
					employeeIndex--;
					continue;
				}
				usedEmployees.add(employeeId);
				employeeIds.add(employeeId);
			}
			bulkAssignmentDto.setEmployeeIds(employeeIds);
			//assignmentService.bulkAssignCycle(bulkAssignmentDto);
		}
	}

}
