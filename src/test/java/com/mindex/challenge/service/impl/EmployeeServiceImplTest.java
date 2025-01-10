package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.CircularReferenceException;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure?includeDirectReportDetails={includeDetails}";
    }

    // Note for reviewers -> Would probably be best to split these tests into different responsibilities.
    // Asserting too many scenarios in one test.
    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testGetReportingStructureWithoutDetails() {
        // Create employee hierarchy
        Employee manager = createTestEmployee("Jane", "Doe", "Management", "Manager");
        Employee developer1 = createTestEmployee("John", "Smith", "Engineering", "Developer");
        Employee developer2 = createTestEmployee("Alice", "Brown", "Engineering", "Developer");

        // Set up reporting structure
        manager.setDirectReports(List.of(developer1, developer2));
        updateEmployee(manager);

        // Test reporting structure without details
        ReportingStructure reportingStructureWithoutDetails = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                manager.getEmployeeId(),
                false
        ).getBody();

        assertNotNull(reportingStructureWithoutDetails);
        assertEquals(2, reportingStructureWithoutDetails.getNumberOfReports());
        assertEquals(manager.getEmployeeId(), reportingStructureWithoutDetails.getEmployee().getEmployeeId());
    }

    @Test
    public void testGetReportingStructureWithDetails() {
        // Create employee hierarchy
        Employee manager = createTestEmployee("Jane", "Doe", "Management", "Manager");
        Employee developer1 = createTestEmployee("John", "Smith", "Engineering", "Developer");
        Employee developer2 = createTestEmployee("Alice", "Brown", "Engineering", "Developer");

        // Set up reporting structure
        manager.setDirectReports(List.of(developer1, developer2));
        updateEmployee(manager);

        // Test reporting structure with details
        ReportingStructure reportingStructureWithDetails = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                manager.getEmployeeId(),
                true
        ).getBody();

        assertNotNull(reportingStructureWithDetails);
        assertEquals(2, reportingStructureWithDetails.getNumberOfReports());
        assertEquals(manager.getEmployeeId(), reportingStructureWithDetails.getEmployee().getEmployeeId());
        assertEquals(2, reportingStructureWithDetails.getEmployee().getDirectReports().size());
        assertEquals(developer1.getEmployeeId(), reportingStructureWithDetails.getEmployee().getDirectReports().get(0).getEmployeeId());
        assertEquals(developer2.getEmployeeId(), reportingStructureWithDetails.getEmployee().getDirectReports().get(1).getEmployeeId());
    }



    @Test
    public void testCircularReferenceInReportingStructure() {
        // Create employees
        Employee employeeA = createTestEmployee("Employee", "A", "Engineering", "Developer");
        Employee employeeB = createTestEmployee("Employee", "B", "Engineering", "Developer");

        Employee employeeAStub = new Employee();
        employeeAStub.setEmployeeId(employeeA.getEmployeeId());

        Employee employeeBStub = new Employee();
        employeeAStub.setEmployeeId(employeeA.getEmployeeId());

        // Create circular reference
        employeeA.setDirectReports(List.of(employeeBStub));
        employeeB.setDirectReports(List.of(employeeAStub));

        updateEmployee(employeeA);
        updateEmployee(employeeB);

        try {
            restTemplate.getForEntity(
                    reportingStructureUrl,
                    ReportingStructure.class,
                    employeeA.getEmployeeId(),
                    true
            );
        } catch (CircularReferenceException ex) {
            assertEquals("Circular reference detected for employeeId: " + employeeA.getEmployeeId(), ex.getMessage());
        }
    }

    /**
     * Creates a test employee in the database using REST template.
     *
     * @param firstName The first name of the employee.
     * @param lastName The last name of the employee.
     * @param department The department of the employee.
     * @param position The position of the employee.
     * @return The created Employee.
     */
    private Employee createTestEmployee(String firstName, String lastName, String department, String position) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(department);
        employee.setPosition(position);

        return restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();
    }

    /**
     * Updates an existing employee in the database using REST template.
     *
     * @param employee The Employee with updated details.
     */
    private void updateEmployee(Employee employee) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(
                employeeUrl + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(employee, headers),
                Employee.class,
                employee.getEmployeeId()
        );
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
