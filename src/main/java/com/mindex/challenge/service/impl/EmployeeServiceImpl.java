package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.exceptions.ResourceNotFoundException;
import com.mindex.challenge.models.Employee;
import com.mindex.challenge.models.requests.DirectReportReferenceDTO;
import com.mindex.challenge.models.requests.EmployeeCreateRequest;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(EmployeeCreateRequest request) {
        LOG.debug("Creating employee [{}]", request);
        final Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPosition(request.getPosition());
        employee.setDepartment(request.getDepartment());
        employee.setDirectReports(mapDirectReports(request.getDirectReports()));
        employee.setEmployeeId(generateEmployeeId());

        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id, boolean includeDirectReportDetails) {
        LOG.debug("Creating employee with id [{}]", id);

        final Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new ResourceNotFoundException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    private List<Employee> mapDirectReports(List<DirectReportReferenceDTO> directReports) {
        return directReports == null
                ? Collections.emptyList() // Return empty list if directReports is null
                : directReports.stream()
                .map(report -> {
                    Employee directReport = new Employee();
                    directReport.setEmployeeId(report.getEmployeeId()); // Only map employeeId
                    return directReport;
                })
                .collect(Collectors.toList());
    }

    private static String generateEmployeeId() {
        return UUID.randomUUID().toString();
    }

}
