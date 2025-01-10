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
    @Id // MongoDB's auto-generated unique identifier
    private String compensationId;

    private String employeeId;

    private LocalDate effectiveDate;

    private BigDecimal salary;

    /**
     * Constructs a new Compensation instance with the specified employee ID, salary, and effective date.
     *
     * @param employeeId the employeeId of this employee.
     * @param salary the salary of this compensation.
     * @param effectiveDate the effective date of this compensation.
     */
    public Compensation(String employeeId, BigDecimal salary, LocalDate effectiveDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

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
