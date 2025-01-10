package com.mindex.challenge.data.requests;

import com.mindex.challenge.validators.ValidLocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Represents the details required for creating a new employee compensation entry.
 *
 * @author Robert Heinbokel
 */
public class CompensationCreateRequest {

    @ValidLocalDate(message = "Effective date must be a valid local date in yyyy-MM-dd format")
    private String effectiveDate;

    @NotNull(message = "Salary must not be null.")
    @DecimalMin(value = "0.0", message = "Salary must be zero or a positive value.")
    private BigDecimal salary;

    // Getters and Setters

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
