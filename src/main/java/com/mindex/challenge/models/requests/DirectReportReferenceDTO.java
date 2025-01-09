package com.mindex.challenge.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * POJO Representing a direct report reference, when creating or updating an {@link com.mindex.challenge.models.Employee}.
 *
 * @author Robert Heinbokel
 */
public class DirectReportReferenceDTO {

    @NotBlank(message = "Direct report employeeId is required.")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Direct report employeeId must be a valid UUID."
    )
    private String employeeId;

    public DirectReportReferenceDTO() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
