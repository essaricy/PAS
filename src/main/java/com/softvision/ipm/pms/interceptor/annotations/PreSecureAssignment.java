package com.softvision.ipm.pms.interceptor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreSecureAssignment {

	boolean permitEmployee() default false;

	boolean permitManager() default false;

}