package com.mindex.challenge.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        try {
            UUID.fromString(value); // Validate UUID format
            return true;
        } catch (IllegalArgumentException ex) {
            return false; // Invalid UUID format
        }
    }
}

