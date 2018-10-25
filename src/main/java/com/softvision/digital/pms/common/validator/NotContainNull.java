package com.softvision.digital.pms.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotContainNullValidator.class)
public @interface NotContainNull {

    String message() default "List cannot contain nulls";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}