package com.mindex.challenge.data;

/**
 * Represents a reporting structure, including direct and indirect reports, for an employee.
 *
 * @author Robert Heinbokel
 */
public class ReportingStructure {

    private Employee employee;
    private int numberOfReports; // Includes direct and indirect reports

    /**
     * Constructs a full ReportingStructure.
     *
     * @param employee the base {@link Employee} of the reporting structure.
     * @param numberOfReports the total number of direct and indirect reports for the employee.
     */
    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    // Getters/Setters below.

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}