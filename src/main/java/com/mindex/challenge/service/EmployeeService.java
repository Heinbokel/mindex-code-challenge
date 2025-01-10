package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);

    /**
     * Generates the reporting structure for a given employee, optionally generating the entire employee hierarchy as well.
     * @param employeeId the employeeId of the employee.
     * @param includeDirectReportDetails indicates whether to generate the entire hierarchy for the employee and all reports.
     * @return The {@link ReportingStructure} of the employee.
     */
    ReportingStructure getReportingStructure(String employeeId, boolean includeDirectReportDetails);
}
