package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.requests.CompensationCreateRequest;
import com.mindex.challenge.service.ICompensationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link CompensationController}.
 *
 * @author Robert Heinbokel
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CompensationController.class)
public class CompensationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICompensationService compensationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private static final String EFFECTIVE_DATE_STRING = "2023-01-01";
    private static final BigDecimal SALARY = new BigDecimal("75000");

    @Test
    public void testReadAll_ValidEmployeeId_ReturnsOkAndValidBody() throws Exception {
        // given
        Compensation compensation = new Compensation(EMPLOYEE_ID, SALARY, LocalDate.parse(EFFECTIVE_DATE_STRING));
        Mockito.when(compensationService.readAll(anyString())).thenReturn(Collections.singletonList(compensation));

        // when/then
        mockMvc.perform(get("/compensation/{employeeId}", EMPLOYEE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].employeeId").value(EMPLOYEE_ID))
                .andExpect(jsonPath("$[0].salary").value(SALARY.toString()))
                .andExpect(jsonPath("$[0].effectiveDate").value(EFFECTIVE_DATE_STRING));
    }

    @Test
    public void testReadByEffectiveDate_ValidRequest_ReturnsOkAndValidBody() throws Exception {
        // given
        Compensation compensation = new Compensation(EMPLOYEE_ID, SALARY, LocalDate.parse(EFFECTIVE_DATE_STRING));
        Mockito.when(compensationService.readByEffectiveDate(anyString(), any(LocalDate.class))).thenReturn(compensation);

        // when/then
        mockMvc.perform(get("/compensation/{employeeId}/effective-date/{effectiveDate}", EMPLOYEE_ID, EFFECTIVE_DATE_STRING))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Ensure JSON response
                .andExpect(jsonPath("$.employeeId").value(EMPLOYEE_ID))
                .andExpect(jsonPath("$.salary").value(SALARY.toString()))
                .andExpect(jsonPath("$.effectiveDate").value(EFFECTIVE_DATE_STRING));
    }

    @Test
    public void testCreate_ValidRequest_ReturnsCreatedAndValidBody() throws Exception {
        // given
        CompensationCreateRequest request = new CompensationCreateRequest();
        request.setEffectiveDate(EFFECTIVE_DATE_STRING);
        request.setSalary(SALARY);

        Compensation expectedCompensation = new Compensation(EMPLOYEE_ID, SALARY, LocalDate.parse(EFFECTIVE_DATE_STRING));
        Mockito.when(compensationService.create(any(CompensationCreateRequest.class), anyString())).thenReturn(expectedCompensation);

        // when/then
        mockMvc.perform(post("/compensation/{employeeId}", EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(EMPLOYEE_ID))
                .andExpect(jsonPath("$.salary").value(SALARY.toString()))
                .andExpect(jsonPath("$.effectiveDate").value(EFFECTIVE_DATE_STRING));
    }

    @Test
    public void testReadAll_InvalidEmployeeId_ReturnsBadRequestAndErrorMessage() throws Exception {
        mockMvc.perform(get("/compensation/{employeeId}", "invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("readAll.employeeId: Employee ID must be a valid UUID"))
                .andExpect(jsonPath("$.message").value("Validation error"));
    }

    @Test
    public void testCreate_InvalidRequest_ReturnsBadRequestAndErrorMessage() throws Exception {
        // given
        CompensationCreateRequest request = new CompensationCreateRequest();
        request.setEffectiveDate(EFFECTIVE_DATE_STRING);
        request.setSalary(null); // Invalid salary

        // when/then
        mockMvc.perform(post("/compensation/{employeeId}", EMPLOYEE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Salary must not be null."))
                .andExpect(jsonPath("$.message").value("Validation error"));
    }
}
