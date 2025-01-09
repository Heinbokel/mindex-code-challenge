package com.mindex.challenge.controller;

import com.mindex.challenge.models.Employee;
import com.mindex.challenge.models.requests.EmployeeCreateRequest;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.validators.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for managing Employee-related operations.
 * Provides endpoints for creating, retrieving, and updating Employee resources.
 *
 * @author Robert Heinbokel
 */
@RequestMapping("/employee")
@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    /**
     * Constructor for dependency injection.
     * @param employeeService The {@link EmployeeService} to use.
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Creates a new employee and assigns them a unique employeeId.
     *
     * @param request the request object containing the details of the employee to be created,
     *                including first name, last name, position, department, and optionally
     *                their direct reports.
     * @return the created Employee, including the assigned unique employeeId.
     */
    @PostMapping()
    @Operation(summary = "Create an employee", description = "Creates a new employee and assigns them a unique employeeId.")
    public Employee create(
            @RequestBody
            @Valid
            @Parameter(description = "The employee details to be created.")
            EmployeeCreateRequest request) {
        LOG.debug("Received employee create request for [{}]", request);

        return employeeService.create(request);
    }

    /**
     * Retrieves an employee by their unique employeeId.
     *
     * @param id the unique identifier of the employee to retrieve.
     * @return the {@link Employee} corresponding to the given employeeId.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an employee", description = "Retrieves an employee by their unique employeeId.")
    public Employee read(
            @PathVariable
            @ValidUUID
            @Parameter(description = "The unique identifier of the employee to retrieve.", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
            String id,
            @RequestParam(required = false, defaultValue = "false")
            @Parameter(description = "Whether to include details of the direct reports of the employee in the response. Defaults to false.")
            boolean includeDirectReportDetails) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id, includeDirectReportDetails);
    }

    /**
     * Updates the details of an existing employee.
     *
     * @param id the unique identifier of the employee to update.
     * @param employee the updated employee details.
     * @return the updated {@link Employee} object.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an employee", description = "Updates an existing employee's details.")
    public Employee update(
            @PathVariable
            @ValidUUID
            @Parameter(description = "The unique identifier of the employee to retrieve.", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
            String id,
            @RequestBody
            @Parameter(description = "The employee details to be updated.")
            Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
}
