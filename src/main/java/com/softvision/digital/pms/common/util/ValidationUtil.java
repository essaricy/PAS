package com.softvision.digital.pms.common.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

public class ValidationUtil {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	private ValidationUtil() {}

	public static <T> void validate(T object) {
		Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
		if (!violations.isEmpty()) {
			throw new ValidationException(violations.iterator().next().getMessage());
		}
	}

}
