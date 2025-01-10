package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an employee's compensation details.
 *
 * @author Robert Heinbokel
 */
public class Compensation {
    @Id
    private String compensationId; // MongoDB's auto-generated unique identifier

    private String employeeId;

    private LocalDate effectiveDate;

    private BigDecimal salary;

    // Getters and Setters

    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
