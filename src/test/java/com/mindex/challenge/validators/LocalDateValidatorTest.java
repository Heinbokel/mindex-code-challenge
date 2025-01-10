package com.mindex.challenge.validators;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LocalDateValidator}.
 *
 * @author Robert Heinbokel
 */
public class LocalDateValidatorTest {

    private LocalDateValidator localDateValidator;

    @Before
    public void setUp() {
        localDateValidator = new LocalDateValidator();
    }

    @Test
    public void testValidLocalDate() {
        // given
        String validDate = "2023-01-01";

        // when
        boolean result = localDateValidator.isValid(validDate, null);

        // then
        assertTrue("A valid date should return true", result);
    }

    @Test
    public void testInvalidLocalDate() {
        // given
        String validDate = "2023-15-55";

        // when
        boolean result = localDateValidator.isValid(validDate, null);

        // then
        assertFalse("An invalid date should return false", result);
    }

    @Test
    public void testInvalidDate_InvalidFormat() {
        // given
        String invalidDate = "01-01-2023"; // Wrong format

        // when
        boolean result = localDateValidator.isValid(invalidDate, null);

        // then
        assertFalse("An incorrectly formatted date should return false", result);
    }

    @Test
    public void testInvalidDate_EmptyString() {
        // given
        String emptyDate = "";

        // when
        boolean result = localDateValidator.isValid(emptyDate, null);

        // then
        assertFalse("An empty string should return false", result);
    }

    @Test
    public void testInvalidDate_BlankString() {
        // given
        String blankDate = "   ";

        // when
        boolean result = localDateValidator.isValid(blankDate, null);

        // then
        assertFalse("A blank string should return false", result);
    }

    @Test
    public void testInvalidDate_Null() {
        // given
        String nullDate = null;

        // when
        boolean result = localDateValidator.isValid(nullDate, null);

        // then
        assertFalse("A null value should return false", result);
    }

    @Test
    public void testValidLeapYearDate() {
        // given
        String leapYearDate = "2024-02-29"; // Valid leap year date

        // when
        boolean result = localDateValidator.isValid(leapYearDate, null);

        // then
        assertTrue("A valid leap year date should return true", result);
    }

}
