package com.softvision.ipm.pms.common.validator;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotContainNullValidator implements ConstraintValidator<NotContainNull, Collection<? extends Object>> {

    @Override
    public void initialize(NotContainNull notEmptyFields) {
    }

    @Override
    public boolean isValid(Collection<? extends Object> objects, ConstraintValidatorContext context) {
    	return (objects != null) ? objects.stream().filter(Objects::isNull).collect(Collectors.toList()).size() == 0 : true;
    }

}