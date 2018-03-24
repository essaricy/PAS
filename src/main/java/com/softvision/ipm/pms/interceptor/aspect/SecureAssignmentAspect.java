package com.softvision.ipm.pms.interceptor.aspect;

import java.util.Calendar;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assign.entity.EmployeePhaseAssignment;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.interceptor.annotations.PreSecureAssignment;

@Aspect
@Component
public class SecureAssignmentAspect {

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Around("@annotation(preSecureAssignment)")
	public Object permit(ProceedingJoinPoint joinPoint, PreSecureAssignment preSecureAssignment) throws Throwable {
		long assignmentId=0;
		int requestedEmployeeId=0;
		System.out.println("******************************************************");
		Object[] args = joinPoint.getArgs();
		System.out.println("args.length?" + args.length);
		if (args.length < 2) {
			throw new ServiceException("Could not obtain assignmentId and requestedEmployeeId");
		}
		assignmentId = (Long) args[0];
		requestedEmployeeId = (Integer) args[1];
		boolean permitEmployee = preSecureAssignment.permitEmployee();
		boolean permitManager = preSecureAssignment.permitManager();
		System.out.println("assignmentId=" + assignmentId + ", requestedEmployeeId=" + requestedEmployeeId
				+ ", permitEmployee=" + permitEmployee + ", permitManager=" + permitManager);

		EmployeePhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
		if (employeePhaseAssignment == null) {
			throw new InvalidInputException("No such assignment exists");
		}
		int phaseId = employeePhaseAssignment.getPhaseId();
		AppraisalPhase appraisalPhase = appraisalPhaseDataRepository.findById(phaseId);
		if (appraisalPhase == null) {
			throw new InvalidInputException("No such appraisal phase");
		}
		Date endDate = appraisalPhase.getEndDate();
		if (endDate.after(Calendar.getInstance().getTime())) {
			throw new InvalidInputException("you cannot modify an assignment from a phase which is in future");
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
		if (!allow) {
			throw new AuthorizationServiceException("UNAUTHORIZED: You are not allowed to access this form");
		}
		System.out.println("Authorized");
	    return joinPoint.proceed();
	}

}
