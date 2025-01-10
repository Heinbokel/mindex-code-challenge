package com.mindex.challenge.models;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Employee {
    @Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;

    /* Review Note - The readme says this should come back as either a list of strings or a list of objects.
     * For the sake of predictability and consistency to all consumers, that is not a good design.
     * Instead, they can be Employees with just the employeeIds, or they can be Employees with all details.
     * We can choose to hide null values or not in the response using @JsonInclude NOT_NULL on this class or a custom object mapper.
     */
    private List<Employee> directReports;


    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }
}
