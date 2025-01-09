package com.mindex.challenge.service;

import com.mindex.challenge.models.Employee;
import com.mindex.challenge.models.requests.EmployeeSaveRequest;

public interface EmployeeService {
    Employee create(EmployeeSaveRequest request);
    Employee read(String id, boolean includeDirectReportDetails);
    Employee update(EmployeeSaveRequest request, String id);
}
