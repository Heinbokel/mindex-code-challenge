package com.mindex.challenge.models.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * POJO Representing a request to create a new Employee.
 *
 * @author Robert Heinbokel.
 */
public class EmployeeCreateRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Department is required")
    private String department;

    @Valid
    private List<DirectReportReferenceDTO> directReports;

    public EmployeeCreateRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<DirectReportReferenceDTO> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<DirectReportReferenceDTO> directReports) {
        this.directReports = directReports;
    }
}
