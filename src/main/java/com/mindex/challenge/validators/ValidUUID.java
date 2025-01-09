package com.mindex.challenge.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UUIDValidator.class) // Link to the validator class
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Applicable to fields and method parameters
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {

    String message() default "Invalid UUID format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
