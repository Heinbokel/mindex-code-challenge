package com.mindex.challenge.service;

import com.mindex.challenge.models.Employee;
import com.mindex.challenge.models.requests.EmployeeCreateRequest;

public interface EmployeeService {
    Employee create(EmployeeCreateRequest request);
    Employee read(String id, boolean includeDirectReportDetails);
    Employee update(Employee employee);
}
