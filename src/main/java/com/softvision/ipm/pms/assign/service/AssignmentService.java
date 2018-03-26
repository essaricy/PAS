package com.softvision.ipm.pms.assign.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.AssignmentRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.DateUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class AssignmentService {

	@Autowired private AssignmentRepository assignmentRepository;

	@Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private EmployeeRepository employeeRepository;

	private String NO_ELIGIBILITY_MESSAGE = "{0} - Not eligible for this appraisal cycle. He/She has joined on {1} which is after the appraisal eligibility cut off date ({2})";

	private String CANNOT_ASSIGN_TO_SELF = "{0} - Cannot assign an appraisal cycle to self";

	private String ALREADY_ASSIGNED = "{0} - An appraisal cycle has already been assigned by {1} on {2}";

	private String ASSIGN_SUCCESSFUL = "{0} - appraisal cycle has been assigned successfully";

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

		AppraisalCycle cycle = appraisalCycleDataRepository.findById(cycleId);
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
			result.setContent(employeeId);
			try {
				if (employeeId == 0) {
					throw new ServiceException("Invalid Employee ID");
				}
				Employee employee = employeeRepository.findByEmployeeId(employeeId);
				if (employee == null) {
					throw new ServiceException("Employee does not exist");
				}
				String employeeName = employee.getFirstName() + " " + employee.getLastName();
				Date hiredOn = employee.getHiredOn();
				Date cutoffDate = cycle.getCutoffDate();
				// check if employee hired date should be before cutoff date
				if (hiredOn.after(cutoffDate)) {
					throw new ServiceException(MessageFormat.format(NO_ELIGIBILITY_MESSAGE, employeeName,
							DateUtil.getIndianDateFormat(hiredOn), DateUtil.getIndianDateFormat(cutoffDate)));
				}
				if (employeeId == assignedBy) {
					throw new ServiceException(MessageFormat.format(CANNOT_ASSIGN_TO_SELF, employeeName));
				}
				// check if already assigned.
				CycleAssignment cycleAssignment = cycleAssignmentDataRepository.findByCycleIdAndEmployeeId(cycleId, employeeId);
				if (cycleAssignment != null) {
					throw new ServiceException(MessageFormat.format(ALREADY_ASSIGNED, employeeName, assignedBy, DateUtil.getIndianDateFormat(assignedAt)));
				}
				cycleAssignment = new CycleAssignment();
				cycleAssignment.setAssignedAt(assignedAt);
				cycleAssignment.setAssignedBy(bulkAssignmentDto.getAssignedBy());
				cycleAssignment.setCycleId(cycleId);
				cycleAssignment.setEmployeeId(employeeId);
				cycleAssignment.setTemplateId(templateId);
				assignmentRepository.assign(cycleAssignment, cycle, employeeName);
				result.setCode(Result.SUCCESS);
				result.setMessage(MessageFormat.format(ASSIGN_SUCCESSFUL , employeeName));
			} catch (Exception exception) {
				result.setCode(Result.FAILURE);
				result.setMessage(exception.getMessage());
			} finally {
				results.add(result);
			}
		}
		return results;
	}
	
	public List<EmployeeAssignmentDto> getAllEmployeeAssignmentsforCycle(int cycleId) {
		List<EmployeeAssignmentDto> employeeAssignments = new ArrayList<>();
		employeeAssignments=assignmentRepository.getAllEmployeeAssignmentsforCycle( cycleId);
		return employeeAssignments;
	}
	
	public List<EmployeeAssignmentDto> getAllEmployeeAssignmentsforPhase(int cycleId,int phaseId) {
		List<EmployeeAssignmentDto> employeeAssignments = new ArrayList<>();
		employeeAssignments=assignmentRepository.getAllEmployeeAssignmentsforPhase( cycleId,phaseId);
		return employeeAssignments;
	}

}
