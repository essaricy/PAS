package com.softvision.digital.pms.assign.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.digital.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.model.BulkAssignmentDto;
import com.softvision.digital.pms.assign.repo.AssignmentRepository;
import com.softvision.digital.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.digital.pms.assign.validator.EligibilityValidator;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.DateUtil;
import com.softvision.digital.pms.common.util.ValidationUtil;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.repo.EmployeeDataRepository;
import com.softvision.digital.pms.template.entity.Template;
import com.softvision.digital.pms.template.repo.TemplateDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
public class AssignmentService {

	@Autowired private AssignmentRepository assignmentRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	private static final String CANNOT_ASSIGN_TO_SELF = "{0} - Cannot assign an appraisal to self";

	private static final String ALREADY_ASSIGNED = "{0} - An appraisal has already been assigned by {1} on {2}";

	private static final String ASSIGN_SUCCESSFUL = "{0} - appraisal has been assigned successfully";

	public List<Result> bulkAssign(@NotNull @Valid BulkAssignmentDto bulkAssignmentDto) throws ServiceException {
		List<Result> results = new ArrayList<>();
		Date assignedAt = Calendar.getInstance().getTime();
		log.info("bulkAssign: START {}", bulkAssignmentDto);
		if (bulkAssignmentDto == null) {
			throw new ServiceException("No Assignments are provided");
		}
		ValidationUtil.validate(bulkAssignmentDto);
		int cycleId = bulkAssignmentDto.getCycleId();
		int phaseId = bulkAssignmentDto.getPhaseId();
		int assignedBy = bulkAssignmentDto.getAssignedBy();
		long templateId = bulkAssignmentDto.getTemplateId();
		List<Integer> employeeIds = bulkAssignmentDto.getEmployeeIds();

		Optional<AppraisalCycle> cycleOptional = appraisalCycleDataRepository.findById(cycleId);
		AppraisalCycle cycle = cycleOptional.orElseThrow(() -> new ServiceException("There is no such cycle with the given id"));

		Optional<AppraisalPhase> phaseOptional = appraisalPhaseDataRepository.findById(phaseId);
		AppraisalPhase phase = phaseOptional.orElseThrow(() -> new ServiceException("There is no such phase with the given id"));

		AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
		if (appraisalCycleStatus != AppraisalCycleStatus.READY
				&& appraisalCycleStatus != AppraisalCycleStatus.ACTIVE) {
			throw new ServiceException("You can assign employees to a cycle when its either READY or ACTIVE");
		}
		Optional<Template> templateOptional = templateDataRepository.findById(templateId);
		templateOptional.orElseThrow(() -> new ServiceException("There is no such template with the given id"));

		for (Integer employeeId : employeeIds) {
			Result result = new Result();
			result.setContent(employeeId);
			try {
				if (employeeId == 0) {
					throw new ServiceException("Invalid Employee ID");
				}
				Optional<Employee> employeeOptional = employeeRepository.findByEmployeeId(employeeId);
				Employee employee = employeeOptional.orElseThrow(() -> new ServiceException("Employee does not exist"));

				String employeeName = employee.getFirstName() + " " + employee.getLastName();
				if (employeeId == assignedBy) {
				    throw new ServiceException(MessageFormat.format(CANNOT_ASSIGN_TO_SELF, employeeName));
				}
				EligibilityValidator.checkEligibility(employee, cycle);

				Optional<PhaseAssignment> phaseAssignmentOptional = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeId(phaseId, employeeId);
				if (phaseAssignmentOptional.isPresent()) {
					throw new ServiceException(MessageFormat.format(ALREADY_ASSIGNED, employeeName, assignedBy, DateUtil.getIndianDateFormat(assignedAt)));
				}
				PhaseAssignment phaseAssignment = new PhaseAssignment();
				phaseAssignment = new PhaseAssignment();
				phaseAssignment.setAssignedAt(assignedAt);
				phaseAssignment.setAssignedBy(bulkAssignmentDto.getAssignedBy());
				phaseAssignment.setEmployeeId(employeeId);
				phaseAssignment.setPhaseId(phaseId);
				phaseAssignment.setTemplateId(templateId);
				assignmentRepository.assign(phaseAssignment, phase, employeeName);
				log.info("bulkAssign: Successful for phaseId={}, employeeId={}, templateId={}, assignedBy={}"
				        , phaseId, employeeId, templateId, bulkAssignmentDto.getAssignedBy());
				result.setCode(Result.SUCCESS);
				result.setMessage(MessageFormat.format(ASSIGN_SUCCESSFUL , employeeName));
			} catch (Exception exception) {
				result.setCode(Result.FAILURE);
				result.setMessage(exception.getMessage());
				log.info("bulkAssign: Failed for phaseId={}, employeeId={}, templateId={}, assignedBy={}, ERROR={}"
                        , phaseId, employeeId, templateId, bulkAssignmentDto.getAssignedBy(), exception.getMessage());
			} finally {
				results.add(result);
			}
		}
		return results;
	}
	
}
