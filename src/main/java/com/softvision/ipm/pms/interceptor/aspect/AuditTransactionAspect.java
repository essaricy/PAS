package com.softvision.ipm.pms.interceptor.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditTransactionAspect {

	@Around("@annotation(com.softvision.ipm.pms.interceptor.annotations.AuditTransaction)")
	public Object auditTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("******************************************************");
		Object[] args = joinPoint.getArgs();
		if (args != null) {
			for (Object arg : args) {
				System.out.println("arg=" + arg);
			}
		}
		System.out.println("Kind=" + joinPoint.getKind());
		System.out.println("Signature=" + joinPoint.getSignature());
		System.out.println("Target=" + joinPoint.getTarget());
		System.out.println("******************************************************");
	    return joinPoint.proceed();
	}

}
