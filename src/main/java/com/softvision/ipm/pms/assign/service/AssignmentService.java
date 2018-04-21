package com.softvision.ipm.pms.assign.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.AssignmentRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.DateUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.employee.constant.EmployeeLocation;
import com.softvision.ipm.pms.employee.constant.EmployeeOrg;
import com.softvision.ipm.pms.employee.constant.EmploymentType;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.mapper.EmployeeMapper;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class AssignmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);

	@Autowired private AssignmentRepository assignmentRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private EmployeeMapper employeeMapper;

	private String NOT_ELIGIBLE_BY_CUTOFF_DATE = "{0} - Not eligible for this appraisal. He/She has joined on {1} which is after the appraisal eligibility cut off date ({2})";

	private String NOT_ELIGIBLE_BY_EMPLOYEE_TYPE = "{0} - Not eligible for this appraisal. His/Her employement type is {1}";

	private String NOT_ELIGIBLE_BY_EMPLOYEE_TYPE_ORG = "{0} - Not eligible for this appraisal. His/Her employement type is {1} and org is {2}";

	private String NOT_ELIGIBLE_BY_LOCATION = "{0} - Not eligible for this appraisal. His/Her location is {1}";

	private String CANNOT_ASSIGN_TO_SELF = "{0} - Cannot assign an appraisal to self";

	private String ALREADY_ASSIGNED = "{0} - An appraisal has already been assigned by {1} on {2}";

	private String ASSIGN_SUCCESSFUL = "{0} - appraisal has been assigned successfully";

	public List<Result> bulkAssign(BulkAssignmentDto bulkAssignmentDto) throws ServiceException {
		List<Result> results = new ArrayList<>();
		Date assignedAt = Calendar.getInstance().getTime();
		LOGGER.info("bulkAssign: START {}", bulkAssignmentDto);
		if (bulkAssignmentDto == null) {
			throw new ServiceException("No Assignments are provided");
		}
		ValidationUtil.validate(bulkAssignmentDto);
		int cycleId = bulkAssignmentDto.getCycleId();
		int phaseId = bulkAssignmentDto.getPhaseId();
		int assignedBy = bulkAssignmentDto.getAssignedBy();
		long templateId = bulkAssignmentDto.getTemplateId();
		List<Integer> employeeIds = bulkAssignmentDto.getEmployeeIds();

		AppraisalCycle cycle = appraisalCycleDataRepository.findById(cycleId);
		if (cycle == null) {
			throw new ServiceException("There is no such cycle with the given id");
		}
		AppraisalPhase phase = appraisalPhaseDataRepository.findById(phaseId);
		if (phase == null) {
			throw new ServiceException("There is no such phase with the given id");
		}
		AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
		if (appraisalCycleStatus != AppraisalCycleStatus.READY
				&& appraisalCycleStatus != AppraisalCycleStatus.ACTIVE) {
			throw new ServiceException("You can assign employees to a cycle when its either READY or ACTIVE");
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
				EmployeeDto employeeDto = employeeMapper.getEmployeeDto(employee);
				String employeeName = employeeDto.getFullName();
				if (employeeId == assignedBy) {
                    throw new ServiceException(MessageFormat.format(CANNOT_ASSIGN_TO_SELF, employeeName));
                }
				EmployeeLocation employeeLocation = EmployeeLocation.get(employee.getLocation());
				if (employeeLocation != EmployeeLocation.INDIA && employeeLocation != EmployeeLocation.AUSTRALIA) {
				    throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_LOCATION, employeeName, employee.getLocation()));
				}
				EmployeeOrg employeeOrg = EmployeeOrg.get(employee.getOrg());
				EmploymentType employmentType = EmploymentType.get(employee.getEmploymentType());
		        if (employmentType != EmploymentType.REGULAR_EMPLOYEE && employmentType != EmploymentType.PROJECT_EMPLOYEE) {
		            throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_EMPLOYEE_TYPE, employeeName,
                            (employmentType == null) ? "UNKNOWN" : employmentType.getName()));
		        }
		        if (employmentType == EmploymentType.PROJECT_EMPLOYEE && employeeOrg != EmployeeOrg.IT) {
		            throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_EMPLOYEE_TYPE_ORG, employeeName,
                            (employmentType == null) ? "UNKNOWN" : employmentType.getName(), employeeOrg));
		        }
                Date hiredOn = employee.getHiredOn();
                Date cutoffDate = cycle.getCutoffDate();
                // check if employee hired date should be before cutoff date
                if (hiredOn.after(cutoffDate)) {
                    throw new ServiceException(MessageFormat.format(NOT_ELIGIBLE_BY_CUTOFF_DATE, employeeName,
                            DateUtil.getIndianDateFormat(hiredOn), DateUtil.getIndianDateFormat(cutoffDate)));
                }
				PhaseAssignment phaseAssignment = phaseAssignmentDataRepository.findByPhaseIdAndEmployeeId(phaseId, employeeId);
				if (phaseAssignment != null) {
					throw new ServiceException(MessageFormat.format(ALREADY_ASSIGNED, employeeName, assignedBy, DateUtil.getIndianDateFormat(assignedAt)));
				}
				phaseAssignment = new PhaseAssignment();
				phaseAssignment.setAssignedAt(assignedAt);
				phaseAssignment.setAssignedBy(bulkAssignmentDto.getAssignedBy());
				phaseAssignment.setEmployeeId(employeeId);
				phaseAssignment.setPhaseId(phaseId);
				phaseAssignment.setTemplateId(templateId);
				assignmentRepository.assign(phaseAssignment, phase, employeeName);
				LOGGER.info("bulkAssign: Successful for phaseId={}, employeeId={}, templateId={}, assignedBy={}"
				        , phaseId, employeeId, templateId, bulkAssignmentDto.getAssignedBy());
				result.setCode(Result.SUCCESS);
				result.setMessage(MessageFormat.format(ASSIGN_SUCCESSFUL , employeeName));
			} catch (Exception exception) {
				result.setCode(Result.FAILURE);
				result.setMessage(exception.getMessage());
				LOGGER.info("bulkAssign: Failed for phaseId={}, employeeId={}, templateId={}, assignedBy={}, ERROR={}"
                        , phaseId, employeeId, templateId, bulkAssignmentDto.getAssignedBy(), exception.getMessage());
			} finally {
				results.add(result);
			}
		}
		return results;
	}
	
	public List<EmployeeAssignmentDto> getAllAssignments(int cycleId) {
		List<EmployeeAssignmentDto> employeeAssignments = new ArrayList<>();
		employeeAssignments=assignmentRepository.getAllAssignments(cycleId);
		return employeeAssignments;
	}
	
	public List<EmployeePhaseAssignmentDto> getAllAssignments(int cycleId,int phaseId) {
		List<EmployeePhaseAssignmentDto> employeeAssignments = new ArrayList<>();
		employeeAssignments=assignmentRepository.getAllAssignments(cycleId, phaseId);
		return employeeAssignments;
	}

}
