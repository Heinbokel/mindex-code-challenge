package com.mindex.challenge.validators;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link UUIDValidator}.
 *
 * @author Robert Heinbokel
 */
public class UUIDValidatorTest {

    private UUIDValidator uuidValidator;

    @Before
    public void setUp() {
        uuidValidator = new UUIDValidator();
    }

    @Test
    public void testValidUUID() {
        // given
        String validUUID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        // when
        boolean result = uuidValidator.isValid(validUUID, null);

        // then
        assertTrue("A valid UUID should return true", result);
    }

    @Test
    public void testInvalidUUID_Format() {
        // given
        String invalidUUID = "invalid-uuid";

        // when
        boolean result = uuidValidator.isValid(invalidUUID, null);

        // then
        assertFalse("An invalid UUID format should return false", result);
    }

    @Test
    public void testInvalidUUID_Null() {
        // given
        String nullUUID = null;

        // when
        boolean result = uuidValidator.isValid(nullUUID, null);

        // then
        assertFalse("A null UUID should return false", result);
    }

    @Test
    public void testInvalidUUID_Blank() {
        // given
        String blankUUID = "   ";

        // when
        boolean result = uuidValidator.isValid(blankUUID, null);

        // then
        assertFalse("A blank UUID should return false", result);
    }

}
