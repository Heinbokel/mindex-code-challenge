package com.mindex.challenge.service;

import com.mindex.challenge.models.Employee;
import com.mindex.challenge.models.requests.EmployeeSaveRequest;

public interface EmployeeService {
    /**
     * Creates a new Employee based on the provided save request.
     *
     * @param request the {@link EmployeeSaveRequest} object containing the details necessary for creating a new employee.
     * @return the newly created {@link Employee}.
     */
    Employee create(EmployeeSaveRequest request);

    /**
     * Retrieves an Employee based on the given employee ID.
     *
     * @param id the employee ID to look up.
     * @param includeDirectReportDetails whether to include details for the employee's direct reports.
     * @return the retrieved {@link Employee}.
     */
    Employee read(String id, boolean includeDirectReportDetails);

    /**
     * Updates an existing Employee based on the provided save request.
     *
     * @param request the {@link EmployeeSaveRequest} object containing the details necessary for creating a new employee.
     * @return the updated {@link Employee}.
     */
    Employee update(EmployeeSaveRequest request, String id);
}
