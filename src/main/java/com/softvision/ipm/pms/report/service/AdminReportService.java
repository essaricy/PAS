package com.softvision.ipm.pms.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.AssignmentRepository;
import com.softvision.ipm.pms.employee.constant.EmployeeLocation;
import com.softvision.ipm.pms.employee.constant.EmployeeOrg;
import com.softvision.ipm.pms.employee.constant.EmploymentType;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.report.Constant.EmployeeAssignmentStatus;
import com.softvision.ipm.pms.report.model.EmployeeAssignmentCount;
import com.softvision.ipm.pms.report.model.PhaseAssignmentCountDto;

@Service
public class AdminReportService {

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private EmployeeDataRepository employeeRepository;

	@Autowired
	private AppraisalRepository appraisalRepository;
	@Autowired
	private AppraisalCycleDataRepository appraisalCycleDataRepository;

	public List<EmployeeAssignmentDto> getAllAssignments(int cycleId) {
		return assignmentRepository.getAllAssignments(cycleId);
	}

	public List<EmployeePhaseAssignmentDto> getAllAssignments(int cycleId, int phaseId) {
		return assignmentRepository.getAllAssignments(cycleId, phaseId);
	}

	public List<PhaseAssignmentCountDto> getEmployeesCurrentAssignmentStatusCounts() {
		AppraisalCycle cycle = appraisalRepository.getActiveCycle();
		if (cycle == null) {
			return null;
		}

		List<Employee> employees = employeeRepository.findAll();

		List<Employee> eligibleEmployees = getEligibleEmployees(employees, cycle.getCutoffDate());

		List<PhaseAssignmentCountDto> phaseAssignmentCountlist = new ArrayList<>();

		for (AppraisalPhase phase : cycle.getPhases()) {
			List<EmployeePhaseAssignmentDto> allAssignments = assignmentRepository.getAllAssignments(cycle.getId(),
					phase.getId());

			List<EmployeeAssignmentCount> employeeStatusCounts = new ArrayList<>();

			setFinalCount(EmployeeAssignmentStatus.NOT_ASSIGNGED, eligibleEmployees.size() - allAssignments.size(),
					employeeStatusCounts);
			
			seEmployeeStatusCounts(allAssignments,employeeStatusCounts);

			PhaseAssignmentCountDto phaseAssignmentCountDto = new PhaseAssignmentCountDto();
			phaseAssignmentCountDto.setAppraisalPhase(phase);
			phaseAssignmentCountDto.setEmployeeAssignmentCounts(employeeStatusCounts);
			phaseAssignmentCountlist.add(phaseAssignmentCountDto);
		}

		return phaseAssignmentCountlist;
	}

	private List<Employee> getEligibleEmployees(List<Employee> employees, Date cutoffDate) {
		Date hiredOn;
		List<Employee> eligibleEmployees = new ArrayList<>();
		for (Employee employee : employees) {
			EmployeeLocation employeeLocation = EmployeeLocation.get(employee.getLocation());
			if (employeeLocation != EmployeeLocation.INDIA && employeeLocation != EmployeeLocation.AUSTRALIA) {
				continue;
			}
			EmployeeOrg employeeOrg = EmployeeOrg.get(employee.getOrg());
			EmploymentType employmentType = EmploymentType.get(employee.getEmploymentType());
			if (employmentType != EmploymentType.REGULAR_EMPLOYEE
					&& employmentType != EmploymentType.PROJECT_EMPLOYEE) {
				continue;
			}
			if (employmentType == EmploymentType.PROJECT_EMPLOYEE && employeeOrg != EmployeeOrg.IT) {
				continue;
			}
			hiredOn = employee.getHiredOn();
			
			// check if employee hired date should be before cutoff date
			if (hiredOn.after(cutoffDate)) {
				continue;
			}
			eligibleEmployees.add(employee);
		}
		return eligibleEmployees;

	}

	public List<Employee> getUnassignedEmployee(int cycleId, int phaseId) {
		AppraisalCycle cycle = appraisalCycleDataRepository.findById(cycleId);

		if (cycle == null) {
			return new ArrayList<>();
		}

		List<Employee> eligibleEmployees = getEligibleEmployees(employeeRepository.findAll(), cycle.getCutoffDate());

		List<EmployeePhaseAssignmentDto> allAssignments = assignmentRepository.getAllAssignments(cycleId, phaseId);

		return getUnassignedEmployees(eligibleEmployees, allAssignments);
	}

	public List<EmployeeAssignmentCount> seEmployeeStatusCounts(List<EmployeePhaseAssignmentDto> allAssignments,
			List<EmployeeAssignmentCount> finalCounts) {

		int notIntiatedCount = 0;
		int selfAppraisalPendingPendingCount = 0;
		int managerReviewPendingCount = 0;
		int managerReviewSubmittedCount = 0;
		int agreedCount = 0;
		int escalatedCount = 0;
		int concludedCount = 0;

		for (EmployeePhaseAssignmentDto employeePhaseAssignmentDto : allAssignments) {
			int statusCode = employeePhaseAssignmentDto.getStatus();
			if (get(statusCode, EmployeeAssignmentStatus.NOT_INITIATED)) {
				notIntiatedCount++;
			} else if (get(statusCode, EmployeeAssignmentStatus.SELF_APPRAISAL_PENDING,
					EmployeeAssignmentStatus.SELF_APPRAISAL_SAVED)) {
				selfAppraisalPendingPendingCount++;
			} else if (get(statusCode, EmployeeAssignmentStatus.MANAGER_REVIEW_PENDING,
					EmployeeAssignmentStatus.MANAGER_REVIEW_SAVED)) {
				managerReviewPendingCount++;
			} else if (get(statusCode, EmployeeAssignmentStatus.MANAGER_REVIEW_SUBMITTED)) {
				managerReviewSubmittedCount++;
			} else if (get(statusCode, EmployeeAssignmentStatus.EMPLOYEE_AGREED)) {
				agreedCount++;
			} else if (get(statusCode, EmployeeAssignmentStatus.EMPLOYEE_ESCALATED)) {
				escalatedCount++;
			} else if (get(statusCode, EmployeeAssignmentStatus.CONCLUDED)) {
				concludedCount++;
			}
		}

		setFinalCount(EmployeeAssignmentStatus.NOT_INITIATED, notIntiatedCount, finalCounts);
		setFinalCount(EmployeeAssignmentStatus.SELF_APPRAISAL_PENDING, selfAppraisalPendingPendingCount, finalCounts);
		setFinalCount(EmployeeAssignmentStatus.MANAGER_REVIEW_PENDING, managerReviewPendingCount, finalCounts);
		setFinalCount(EmployeeAssignmentStatus.MANAGER_REVIEW_SUBMITTED, managerReviewSubmittedCount, finalCounts);
		setFinalCount(EmployeeAssignmentStatus.EMPLOYEE_AGREED, agreedCount, finalCounts);
		setFinalCount(EmployeeAssignmentStatus.EMPLOYEE_ESCALATED, escalatedCount, finalCounts);
		setFinalCount(EmployeeAssignmentStatus.CONCLUDED, concludedCount, finalCounts);

		return finalCounts;
	}

	public List<Employee> getUnassignedEmployees(List<Employee> eligibleEmployees,
			List<EmployeePhaseAssignmentDto> allAssignments) {
		Boolean addFlag = false;
		List<Employee> unassignedEmployees = new ArrayList<>();
		for (Employee employee : eligibleEmployees) {
			addFlag = true;
			for (EmployeePhaseAssignmentDto employeePhaseAssignmentDto : allAssignments) {
				if (employeePhaseAssignmentDto.getAssignedTo().getEmployeeId() == employee.getEmployeeId()) {
					addFlag = false;
					break;
				}
			}
			if (addFlag)
				unassignedEmployees.add(employee);
		}

		return unassignedEmployees;
	}

	private boolean get(int statusCode, EmployeeAssignmentStatus... list) {
		for (EmployeeAssignmentStatus eAssignmentStatus : list) {
			if (statusCode == eAssignmentStatus.getCode()) {
				return true;
			}
		}
		return false;
	}
	
	private void setFinalCount(final EmployeeAssignmentStatus status, final int count,
			List<EmployeeAssignmentCount> finalCounts) {
		final EmployeeAssignmentCount countDto = new EmployeeAssignmentCount();
		countDto.setEmployeeAssignmentStatus(status);
		countDto.setCount(count);
		finalCounts.add(countDto);
	}

}
