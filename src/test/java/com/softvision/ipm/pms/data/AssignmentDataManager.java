package com.softvision.ipm.pms.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.service.AssignmentService;
import com.softvision.ipm.pms.assign.service.ManagerAssignmentService;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.employee.service.EmployeeService;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.service.TemplateService;

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
		System.out.println("activeCycle1=" + activeCycle);
		System.out.println(Roles.MANAGER.getCode());
		List<EmployeeDto> managers = employeeService.roleSearch(Roles.MANAGER.getName());
		System.out.println("managers= "  + managers);
		List<EmployeeDto> employees = employeeService.getEmployees();
		System.out.println("employees= "  + employees.size());

		for (int managerIndex = 0; managerIndex < numberOfManagers; managerIndex++) {
			System.out.println("Assigning for manager " + managerIndex);
			EmployeeDto randomManager = managers.get(RANDOM.nextInt(managers.size()));
			int managerId = randomManager.getEmployeeId();
			if (usedManagers.contains(managerId)) {
				managerIndex--;
				continue;
			}
			usedManagers.add(managerId);
			System.out.println("Selected Manager: " + managerId);

			TemplateDto randomTemplate = templates.get(RANDOM.nextInt(templates.size()));
			System.out.println("Random Template: " + randomTemplate.getName());

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
				System.out.println("Will assign employee: " + employeeId);
			}
			bulkAssignmentDto.setEmployeeIds(employeeIds);
			System.out.println("bulkAssignmentDto= " + bulkAssignmentDto);
			//assignmentService.bulkAssignCycle(bulkAssignmentDto);
		}
	}

}
