package org.jeff.security.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IPConstraint.class)
public @interface IPValid {

    String message() default "{IPValid.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
