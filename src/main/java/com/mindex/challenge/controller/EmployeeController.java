package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    /**
     * Constructor for dependency injection.
     * @param employeeService the {@link EmployeeService} to use.
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Note to reviewers - I would utilize DTO pattern here and for the PUT method as I have done for Compensation.
    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    /* Note to reviewers - README mentions flexibility in returned direct reports as they can be a String or Employee collection.
     * I feel this is bad practice as consumers of this API should expect consistency in the responses and structure.
     * That being said, an optional boolean, which defaults to false, here and any other endpoints where employees are retrieved,
     * would allow for the direct reports to come back as just the employeeIds or with their entire data filled out (Employee).
     * This way the caller can have a list of { employeeId: string } or { Employee object } depending on what they need.
     * Optionally skipping extra database calls if not needed. This is implemented in the reporting structure endpoint.
     */
    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /**
     * Generates the reporting structure for a given employee, optionally generating the entire employee hierarchy as well.
     * @param id the employeeId of the employee.
     * @param includeDirectReportDetails indicates whether to generate the entire hierarchy for the employee and all reports.
     * @return The {@link ReportingStructure} of the employee.
     */
    @GetMapping("/employee/{id}/reporting-structure")
    public ReportingStructure getReportingStructure(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDirectReportDetails) {
        LOG.debug("Received request to get reporting structure for employee with ID: [{}], direct report details included: [{}]", id, includeDirectReportDetails);

        return employeeService.getReportingStructure(id, includeDirectReportDetails);
    }
}
