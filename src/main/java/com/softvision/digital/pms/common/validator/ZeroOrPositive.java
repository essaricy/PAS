package com.softvision.digital.pms.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZeroOrPositiveValidator.class)
public @interface ZeroOrPositive {

    String message() default "Value should be zero or postive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}