package org.jeff.spring.security.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class IPConstraint implements ConstraintValidator<IPValid, String> {

    @Override
    public void initialize(IPValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value)) {
            //TODO 验证IP
            return true;
        }
        return false;
    }
}

