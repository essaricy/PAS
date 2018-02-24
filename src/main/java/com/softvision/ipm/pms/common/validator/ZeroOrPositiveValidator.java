package com.softvision.ipm.pms.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZeroOrPositiveValidator implements ConstraintValidator<ZeroOrPositive, Long> {

    @Override
    public void initialize(ZeroOrPositive zeroOrPositive) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return (value != null && value >= 0);
    }

}