package com.softvision.ipm.pms.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assess.entity.AssessDetail;
import com.softvision.ipm.pms.assess.entity.AssessHeader;
import com.softvision.ipm.pms.assess.repo.AssessmentHeaderDataRepository;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.model.AssignmentSummaryDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.AssignmentRepository;
import com.softvision.ipm.pms.assign.validator.EligibilityValidator;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.mapper.EmployeeMapper;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.report.Constant.PhaseAssignmentReportStatus;
import com.softvision.ipm.pms.report.model.EmployeeAssignmentCount;
import com.softvision.ipm.pms.report.model.PhaseAssignmentCountDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminReportService {

	@Autowired private AssignmentRepository assignmentRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private AssessmentHeaderDataRepository assessmentHeaderDataRepository;

	@Autowired private EmployeeMapper employeeMapper;

	public List<EmployeeAssignmentDto> getAllAssignments(int cycleId) {
		return assignmentRepository.getAllAssignments(cycleId);
	}

	public List<EmployeePhaseAssignmentDto> getAllAssignments(int cycleId, int phaseId) {
		List<Employee> allEmployees = employeeRepository.findEveryone();
		List<EmployeePhaseAssignmentDto> allAssignments = assignmentRepository.getAllAssignments(cycleId, phaseId);
		if (allAssignments != null && !allAssignments.isEmpty()) {
			allAssignments.stream().forEach(assignment -> {
				EmployeeDto assignedTo = assignment.getAssignedTo();

				Optional<Employee> optional = allEmployees.stream().filter(e -> e.getEmployeeId() == assignedTo.getEmployeeId()).findFirst();
				if (optional.isPresent()) {
					EmployeeDto employeeDto = employeeMapper.getEmployeeDto(optional.get());
					assignment.setAssignedTo(employeeDto);
				}
			});
		}
		return allAssignments;
	}

	public List<PhaseAssignmentCountDto> getEmployeesCurrentAssignmentStatusCounts() {
		log.info("getEmployeesCurrentAssignmentStatusCounts");
		AppraisalCycle cycle = appraisalRepository.getActiveCycle();
		if (cycle == null) {
			return null;
		}

		List<PhaseAssignmentCountDto> phaseAssignmentCountlist = new ArrayList<>();
		for (AppraisalPhase phase : cycle.getPhases()) {
			List<EmployeePhaseAssignmentDto> allAssignments = assignmentRepository.getAllAssignments(cycle.getId(), phase.getId());
			List<EmployeeAssignmentCount> employeeStatusCounts = new ArrayList<>();

			List<EmployeeDto> unassignedEmployee = getUnassignedEmployee(cycle.getId(), phase.getId());
			addEmployeeAssignmentCount(PhaseAssignmentReportStatus.NOT_ASSIGNGED, unassignedEmployee.size(), employeeStatusCounts);

			seEmployeeStatusCounts(allAssignments, employeeStatusCounts);

			PhaseAssignmentCountDto phaseAssignmentCountDto = new PhaseAssignmentCountDto();
			phaseAssignmentCountDto.setAppraisalPhase(phase);
			phaseAssignmentCountDto.setEmployeeAssignmentCounts(employeeStatusCounts);
			phaseAssignmentCountlist.add(phaseAssignmentCountDto);
		}

		return phaseAssignmentCountlist;
	}

	private List<Employee> getEligibleEmployees(List<Employee> employees, final Date cutoffDate) {
		return employees.stream().filter(e -> {
			try { EligibilityValidator.checkEligibility(e, cutoffDate); return true; } catch (ServiceException exception) {}
			return false;
		}).collect(Collectors.toList());
	}

	public List<EmployeeDto> getUnassignedEmployee(int cycleId, int phaseId) {
		AppraisalCycle cycle = appraisalCycleDataRepository.findById(cycleId);
		if (cycle == null) {
			return new ArrayList<>();
		}
		List<Employee> eligibleEmployees = getEligibleEmployees(employeeRepository.findAll(), cycle.getCutoffDate());
		List<EmployeePhaseAssignmentDto> allAssignments = assignmentRepository.getAllAssignments(cycleId, phaseId);

		List<Employee> unassignedEmployees = eligibleEmployees.stream().filter(e -> {
			Optional<EmployeePhaseAssignmentDto> optional = allAssignments.stream().filter(assignment -> assignment.getAssignedTo().getEmployeeId() == e.getEmployeeId()).findAny();
			return !optional.isPresent();
		}).collect(Collectors.toList());
		return employeeMapper.getEmployeeDtoList(unassignedEmployees);
	}

	public List<EmployeeAssignmentCount> seEmployeeStatusCounts(List<EmployeePhaseAssignmentDto> allAssignments,
			List<EmployeeAssignmentCount> finalCounts) {

		int notIntiatedCount = 0;
		int selfAppraisalPendingCount = 0;
		int selfAppraisalRevertedCount = 0;
		int managerReviewPendingCount = 0;
		int managerReviewSubmittedCount = 0;
		int agreedCount = 0;
		int escalatedCount = 0;
		int concludedCount = 0;

		for (EmployeePhaseAssignmentDto employeePhaseAssignmentDto : allAssignments) {
			int statusCode = employeePhaseAssignmentDto.getStatus();
			if (get(statusCode, PhaseAssignmentReportStatus.NOT_INITIATED)) {
				notIntiatedCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.SELF_APPRAISAL_PENDING,
					PhaseAssignmentReportStatus.SELF_APPRAISAL_SAVED)) {
				selfAppraisalPendingCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.SELF_APPRAISAL_REVERTED)) {
				selfAppraisalRevertedCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.MANAGER_REVIEW_PENDING,
					PhaseAssignmentReportStatus.MANAGER_REVIEW_SAVED)) {
				managerReviewPendingCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.MANAGER_REVIEW_SUBMITTED)) {
				managerReviewSubmittedCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.EMPLOYEE_AGREED)) {
				agreedCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.EMPLOYEE_ESCALATED)) {
				escalatedCount++;
			} else if (get(statusCode, PhaseAssignmentReportStatus.CONCLUDED)) {
				concludedCount++;
			}
		}
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.NOT_INITIATED, notIntiatedCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.SELF_APPRAISAL_PENDING, selfAppraisalPendingCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.SELF_APPRAISAL_REVERTED, selfAppraisalRevertedCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.MANAGER_REVIEW_PENDING, managerReviewPendingCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.MANAGER_REVIEW_SUBMITTED, managerReviewSubmittedCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.EMPLOYEE_AGREED, agreedCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.EMPLOYEE_ESCALATED, escalatedCount, finalCounts);
		addEmployeeAssignmentCount(PhaseAssignmentReportStatus.CONCLUDED, concludedCount, finalCounts);

		return finalCounts;
	}

	private boolean get(int statusCode, PhaseAssignmentReportStatus... list) {
		for (PhaseAssignmentReportStatus eAssignmentStatus : list) {
			if (statusCode == eAssignmentStatus.getCode()) {
				return true;
			}
		}
		return false;
	}
	
	private void addEmployeeAssignmentCount(final PhaseAssignmentReportStatus status, final int count,
			List<EmployeeAssignmentCount> finalCounts) {
		final EmployeeAssignmentCount countDto = new EmployeeAssignmentCount();
		countDto.setPhaseAssignmentReportStatus(status);
		countDto.setCount(count);
		finalCounts.add(countDto);
	}

	public List<AssignmentSummaryDto> getAllEmployeesPhaseScore(int cycleId, int phaseId) {
		log.info("getAllEmployeesPhaseScore: cycleId={}, phaseId={}", cycleId, phaseId);

		List<AssignmentSummaryDto> assignmentSummaryDtoList = new ArrayList<>();
		List<EmployeePhaseAssignmentDto> employeePhaseAssignments = getAllAssignments(cycleId, phaseId);
		employeePhaseAssignments.stream().forEach(employeePhaseAssignment -> {
			long assignmentId = employeePhaseAssignment.getAssignmentId();
			EmployeeDto assignedTo = employeePhaseAssignment.getAssignedTo();
			EmployeeDto assignedBy = employeePhaseAssignment.getAssignedBy();
			PhaseAssignmentStatus status = PhaseAssignmentStatus.get(employeePhaseAssignment.getStatus());

			double selfRating=0;
			double managerRating=0;
			if (status == PhaseAssignmentStatus.NOT_INITIATED
					|| status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
					|| status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED
					|| status == PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED) {
				
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING
					|| status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
				List<AssessHeader> assessHeaders = assessmentHeaderDataRepository.findByAssignIdOrderByStageAsc(assignmentId);
				selfRating=getRating(assessHeaders, 0);
				managerRating=0;
			} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED
					|| status == PhaseAssignmentStatus.EMPLOYEE_AGREED
					|| status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED
					|| status == PhaseAssignmentStatus.CONCLUDED) {
				List<AssessHeader> assessHeaders = assessmentHeaderDataRepository.findByAssignIdOrderByStageAsc(assignmentId);
				selfRating=getRating(assessHeaders, 0);
				managerRating=getRating(assessHeaders, 1);
			}

			AssignmentSummaryDto assignmentSummaryDto = new AssignmentSummaryDto();
			assignmentSummaryDto.setAssignedAt(employeePhaseAssignment.getAssignedAt());
			assignmentSummaryDto.setAssignedBy(assignedBy);
			assignmentSummaryDto.setAssignedTo(assignedTo);
			assignmentSummaryDto.setAssignmentId(assignmentId);
			assignmentSummaryDto.setManagerScore(managerRating);
			assignmentSummaryDto.setScore(selfRating);
			assignmentSummaryDto.setStatus(employeePhaseAssignment.getStatus());
			assignmentSummaryDtoList.add(assignmentSummaryDto);
			//assignmentSummaryDto.setTemplate(template);
		});
		return assignmentSummaryDtoList;
	}

	private double getRating(List<AssessHeader> assessHeaders, int stage) {
		for (AssessHeader assessHeader : assessHeaders) {
			if (stage == assessHeader.getStage()) {
				double goalsSum = 0;
				List<AssessDetail> assessDetails = assessHeader.getAssessDetails();
				for (AssessDetail assessDetail : assessDetails) {
					double score = assessDetail.getScore();
					goalsSum = goalsSum + score;
				}
				return goalsSum;
			}
		}
		return -1;
	}

}
