package com.softvision.ipm.pms.assign.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assign.entity.EmployeeCycleAssignment;
import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.repo.AssignmentCycleDataRepository;
import com.softvision.ipm.pms.assign.repo.AssignmentRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class AssignmentService {

	@Autowired private AssignmentRepository assignmentRepository;

	@Autowired private AssignmentCycleDataRepository assignmentDataRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private EmployeeRepository employeeRepository;

	public List<Result> bulkAssign(BulkAssignmentDto bulkAssignmentDto) throws ServiceException {
		List<Result> results = new ArrayList<>();
		Date assignedAt = Calendar.getInstance().getTime();

		if (bulkAssignmentDto == null) {
			throw new ServiceException("No Assignments are provided.");
		}
		ValidationUtil.validate(bulkAssignmentDto);
		int cycleId = bulkAssignmentDto.getCycleId();
		int assignedBy = bulkAssignmentDto.getAssignedBy();
		long templateId = bulkAssignmentDto.getTemplateId();
		List<Integer> employeeIds = bulkAssignmentDto.getEmployeeIds();

		AppraisalCycle cycle = appraisalCycleDataRepository.findById((long)cycleId);
		if (cycle == null) {
			throw new ServiceException("There is no such cycle with the given id");
		}
		AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
		if (appraisalCycleStatus != AppraisalCycleStatus.ACTIVE) {
			throw new ServiceException("You can assign employees to a cycle when the its ACTIVE");
		}
		Template template = templateDataRepository.findById(templateId);
		if (template == null) {
			throw new ServiceException("There is no such template with the given id");
		}
		for (Integer employeeId : employeeIds) {
			Result result = new Result();
			try {
				if (employeeId == 0) {
					throw new ServiceException("Invalid Employee ID");
				}
				System.out.println("Doing for " + employeeId);
				Employee employee = employeeRepository.findByEmployeeId(employeeId);
				if (employee == null) {
					throw new ServiceException("Employee does not exist");
				}
				System.out.println("Employee exists");
				String employeeName = employee.getFirstName() + " " + employee.getLastName();
				Date hiredOn = employee.getHiredOn();
				Date cutoffDate = cycle.getCutoffDate();
				// check if employee hired date should be before cutoff date
				if (hiredOn.after(cutoffDate)) {
					throw new ServiceException("Employee (" + employeeName + ") - Not eligible for this appraisal cycle. His Joined date is " + hiredOn + " and appraisal eligibility cut off date is " + cutoffDate);
				}
				if (employeeId == assignedBy) {
					throw new ServiceException("Employee (" + employeeName + ") - Cannot assign an appraisal cycle to himself");
				}
				// check if already assigned.
				EmployeeCycleAssignment cycleAssignment = assignmentDataRepository.findByCycleIdAndTemplateIdAndEmployeeId(cycleId, templateId, employeeId);
				System.out.println("cycleAssignment=" + cycleAssignment);
				if (cycleAssignment != null) {
					throw new ServiceException("Employee (" + employeeName + ") - An appraisal cycle has already been assigned by " + cycleAssignment.getAssignedBy() + " on " + cycleAssignment.getAssignedAt());
				}
				cycleAssignment = new EmployeeCycleAssignment();
				cycleAssignment.setAssignedAt(assignedAt);
				cycleAssignment.setAssignedBy(bulkAssignmentDto.getAssignedBy());
				cycleAssignment.setCycleId(cycleId);
				cycleAssignment.setEmployeeId(employeeId);
				cycleAssignment.setTemplateId(templateId);
				assignmentRepository.assign(cycleAssignment, cycle, employeeName);
				System.out.println("Assignment is completed");
				result.setCode(Result.SUCCESS);
				result.setMessage("Employee (" + employeeName + ") - appraisal cycle has been assigned successfully");
			} catch (Exception exception) {
				result.setCode(Result.FAILURE);
				result.setMessage(exception.getMessage());
			} finally {
				results.add(result);
			}
		}
		return results;
	}

}
