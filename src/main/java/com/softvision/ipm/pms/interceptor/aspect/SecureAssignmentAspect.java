package com.softvision.ipm.pms.interceptor.aspect;

import java.util.Calendar;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.interceptor.annotations.PreSecureAssignment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class SecureAssignmentAspect {

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Around("@annotation(preSecureAssignment)")
	public Object permit(ProceedingJoinPoint joinPoint, PreSecureAssignment preSecureAssignment) throws Throwable {
		long assignmentId=0;
		int requestedEmployeeId=0;
		Object[] args = joinPoint.getArgs();
		if (args.length < 2) {
			throw new ServiceException("Could not obtain assignmentId and requestedEmployeeId");
		}
		assignmentId = (Long) args[0];
		requestedEmployeeId = (Integer) args[1];
		log.info("permit: assignmentId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);

		boolean permitEmployee = preSecureAssignment.permitEmployee();
		boolean permitManager = preSecureAssignment.permitManager();

		PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new ServiceException("No such assignment exists");
		}
		int phaseId = employeePhaseAssignment.getPhaseId();
		AppraisalPhase appraisalPhase = appraisalPhaseDataRepository.findById(phaseId);
		if (appraisalPhase == null) {
			throw new ServiceException("No such appraisal phase");
		}
		Date endDate = appraisalPhase.getEndDate();
		if (endDate.after(Calendar.getInstance().getTime())) {
			throw new ServiceException("you cannot modify an assignment from a phase which is in future");
		}
		// permit this form only to the employee and to the manager to whom its been assigned
		int employeeId = employeePhaseAssignment.getEmployeeId();
		int assignedBy = employeePhaseAssignment.getAssignedBy();
		boolean allow=false;
		if (permitEmployee && requestedEmployeeId == employeeId) {
			allow=true;
		}
		if (permitManager && requestedEmployeeId == assignedBy) {
			allow=true;
		}
		log.info("permit: assignmentId={}, requestedEmployeeId={}, allow={}", assignmentId, requestedEmployeeId, allow);
		if (!allow) {
			log.warn("permit: UNAUTHORIZED ACCESS ATTEMPT: assignmentId={}, requestedEmployeeId={}", assignmentId, requestedEmployeeId);
			throw new AuthorizationServiceException("UNAUTHORIZED: You are not allowed to access this form");
		}
	    return joinPoint.proceed();
	}

}
