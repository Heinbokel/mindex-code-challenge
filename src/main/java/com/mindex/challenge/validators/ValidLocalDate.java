package com.mindex.challenge.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a custom annotation for validating local date strings.
 * This annotation is used to validate whether a given string adheres to the format "yyyy-MM-dd"
 * and represents a valid date. The validation logic is implemented in the {@link LocalDateValidator} class.
 *
 * @author Robert Heinbokel
 */
@Constraint(validatedBy = LocalDateValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocalDate {
    String message() default "Date must be in the format yyyy-MM-dd and a valid date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
