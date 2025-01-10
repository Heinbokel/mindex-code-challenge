package com.mindex.challenge.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validates whether a given string conforms to the yyyy-MM-dd format and can be parsed to a local date.
 * This class implements the {@link ConstraintValidator} interface for the custom
 * {@link ValidLocalDate} annotation, providing validation logic for local date strings.
 * The validation ensures that the input string is not null or blank and can
 * be parsed into a valid {@link LocalDate}. If the string cannot be parsed or does not
 * adhere to the date format, the validation fails.
 *
 * @author Robert Heinbokel
 */
public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, String> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            // Parse the date to ensure it is valid
            LocalDate.parse(value, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
