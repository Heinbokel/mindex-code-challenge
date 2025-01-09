package com.mindex.challenge.models.requests;

import com.mindex.challenge.validators.ValidUUID;

/**
 * POJO Representing a direct report reference, when creating or updating an {@link com.mindex.challenge.models.Employee}.
 *
 * @author Robert Heinbokel
 */
public class DirectReportReferenceDTO {

    @ValidUUID
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
