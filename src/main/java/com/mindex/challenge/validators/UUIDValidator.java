package com.mindex.challenge.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

/**
 * Validates whether a given string conforms to the UUID format.
 * This class implements the {@link ConstraintValidator} interface for the custom
 * {@link ValidUUID} annotation, providing validation logic for UUID strings.
 * The validation ensures that the input string is not null or blank and can
 * be parsed into a valid {@link UUID}. If the string cannot be parsed or does not
 * adhere to the UUID format, the validation fails.
 *
 * @author Robert Heinbokel
 */
public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}