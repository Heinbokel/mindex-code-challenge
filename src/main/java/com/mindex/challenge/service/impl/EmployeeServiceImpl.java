package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.CircularReferenceException;
import com.mindex.challenge.exceptions.ResourceNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for dependency injection.
     * @param employeeRepository the {@link EmployeeRepository} to use.
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        // Note to reviewers - try/catches could be done on all database accesses to allow for
        // better/more specific error handling using custom exceptions/messages.
        // This is implemented in the Compensation service.

        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new ResourceNotFoundException(String.format("Employee not found for employeeId: %s", id));
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReportingStructure(String employeeId, boolean includeDirectReportDetails) {
        LOG.debug("Building reporting structure for employeeId [{}]", employeeId);

        // Step 1: Retrieve the root employee from the provided ID.
        final Employee rootEmployee = this.read(employeeId);

        // Initialize a map to cache employee details so we don't call the database unnecessarily.
        final Map<String, Employee> memoizedEmployees = new ConcurrentHashMap<>();

        // Initialize set to track in progress employees and break out of circular references.
        final Set<String> inProgress = new HashSet<>();

        // Step 2: Optionally build out the employee's entire reporting structure.
        if (includeDirectReportDetails) {
            buildEntireEmployeeReportingHierarchy(rootEmployee, memoizedEmployees, inProgress);
        }

        // Step 3: Calculate total count of direct and indirect reports.
        final int numberOfReports = countReports(rootEmployee, memoizedEmployees, inProgress);

        // Step 4: Return reporting structure.
        return new ReportingStructure(rootEmployee, numberOfReports);
    }

    /**
     * Builds out the entire reporting hierarchy for the given employee.
     * @param employee the {@link Employee} to build the entire reporting hierarchy for.
     * @param memoizedEmployees the {@link Map} holding already retrieved employees.
     * @param inProgress the {@link Set<String>} holding in progress employees, allowing the ability to break out of infinite recursion.
     */
    private void buildEntireEmployeeReportingHierarchy(Employee employee, Map<String, Employee> memoizedEmployees, Set<String> inProgress) {
        LOG.debug("Building entire hierarchy for employeeId [{}]", employee.getEmployeeId());

        // Detect circular references.
        checkForCircularReferences(inProgress, employee.getEmployeeId());

        // Add the employee to the in progress set.
        inProgress.add(employee.getEmployeeId());

        // If the employee has no direct reports, break out.
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            inProgress.remove(employee.getEmployeeId());
            return;
        }

        // Stream through the list of direct report stubs, fetch their full details, and recursively build their own hierarchy.
        employee.setDirectReports(
                employee.getDirectReports()
                        .stream()
                        .map(reportStub -> {
                            String reportId = reportStub.getEmployeeId();

                            // Retrieve/return fully populated Employee for this direct report.
                            return memoizedEmployees.computeIfAbsent(reportId, this::read);
                        })
                        // Recursively build out every direct report of this report.
                        .peek(report -> buildEntireEmployeeReportingHierarchy(report, memoizedEmployees, inProgress))
                        .toList()
        );

        // Remove the employee from the in progress set after processing.
        inProgress.remove(employee.getEmployeeId());
    }

    /**
     * Counts all direct and indirect reports of the given employee.
     * @param employee the {@link Employee} to count direct/indirect reports for.
     * @param memoizedEmployees the {@link Map} holding already retrieved employees.
     * @param inProgress the {@link Set<String>} holding in progress employees, allowing the ability to break out of infinite recursion.
     * @return the count of the direct/indirect reports.
     */
    private int countReports(Employee employee, Map<String, Employee> memoizedEmployees, Set<String> inProgress) {
        LOG.debug("Counting reports for employeeId [{}]", employee.getEmployeeId());

        // If there are no direct reports just return 0.
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return 0;
        }

        // Initialize counter.
        int count = 0;

        // Loop through each direct report stub and calculate their reports recursively.
        for (Employee reportStub : employee.getDirectReports()) {
            final String reportId = reportStub.getEmployeeId();

            // Detect circular references to prevent infinite recursion.
            checkForCircularReferences(inProgress, reportId);

            // Add the current report to the in progress set to track recursion.
            inProgress.add(reportId);

            // Retrieve the full details of the given direct report.
            Employee fullReport = memoizedEmployees.computeIfAbsent(reportId, this::read);

            // Add 1 for the current report and recursively count their own reports.
            count += 1 + countReports(fullReport, memoizedEmployees, inProgress);

            // Remove the current report from the in progress set after processing.
            inProgress.remove(reportId);
        }

        return count;
    }

    /**
     * Checks for circular references in a set of in progress employee IDs from direct reports.
     * If a circular reference is detected for the provided employeeId, an error is logged, and an exception is thrown.
     *
     * @param inProgress a {@link Set<String>} of report IDs currently being processed.
     * @param reportId the report ID to check for circular references.
     */
    private static void checkForCircularReferences(Set<String> inProgress, String reportId) {
        if (inProgress.contains(reportId)) {
            throw new CircularReferenceException("Circular reference detected for employeeId: " + reportId);
        }
    }
}
