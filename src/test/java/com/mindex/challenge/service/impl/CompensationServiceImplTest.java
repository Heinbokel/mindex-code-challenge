package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.ICompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.requests.CompensationCreateRequest;
import com.mindex.challenge.exceptions.DuplicateEntityException;
import com.mindex.challenge.exceptions.ResourceNotFoundException;
import com.mindex.challenge.exceptions.UnexpectedDatabaseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link CompensationServiceImpl}.
 *
 * @author Robert Heinbokel
 */
public class CompensationServiceImplTest {

    private static final String EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private static final LocalDate EFFECTIVE_DATE = LocalDate.of(2025, 1, 1);
    private static final BigDecimal SALARY = BigDecimal.valueOf(75000);

    @Mock
    private ICompensationRepository compensationRepository;

    private CompensationServiceImpl compensationService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        compensationService = new CompensationServiceImpl(compensationRepository);
    }

    @Test
    public void testCreate_Success() {
        // given
        CompensationCreateRequest request = new CompensationCreateRequest();
        request.setEffectiveDate(EFFECTIVE_DATE.toString());
        request.setSalary(SALARY);

        Compensation expectedCompensation = new Compensation(EMPLOYEE_ID, SALARY, EFFECTIVE_DATE);

        when(compensationRepository.findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE)).thenReturn(null);
        when(compensationRepository.insert(any(Compensation.class))).thenReturn(expectedCompensation);

        // when
        Compensation result = compensationService.create(request, EMPLOYEE_ID);

        // then
        assertNotNull(result);
        assertEquals(EMPLOYEE_ID, result.getEmployeeId());
        assertEquals(SALARY, result.getSalary());
        assertEquals(EFFECTIVE_DATE, result.getEffectiveDate());

        verify(compensationRepository).findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);
        verify(compensationRepository).insert(any(Compensation.class));
    }

    @Test
    public void testReadAll_Success() {
        // given
        Compensation expectedCompensation = new Compensation(EMPLOYEE_ID, SALARY, EFFECTIVE_DATE);

        when(compensationRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(Collections.singletonList(expectedCompensation));

        // when
        List<Compensation> result = compensationService.readAll(EMPLOYEE_ID);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedCompensation, result.get(0));

        verify(compensationRepository).findByEmployeeId(EMPLOYEE_ID);
    }

    @Test
    public void testReadByEffectiveDate_Success() {
        // given
        Compensation expectedCompensation = new Compensation(EMPLOYEE_ID, SALARY, EFFECTIVE_DATE);

        when(compensationRepository.findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE)).thenReturn(expectedCompensation);

        // when
        Compensation result = compensationService.readByEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);

        // then
        assertNotNull(result);
        assertEquals(expectedCompensation, result);

        verify(compensationRepository).findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);
    }

    @Test
    public void testCreate_DuplicateCompensation_ThrowsException() {
        // given
        CompensationCreateRequest request = new CompensationCreateRequest();
        request.setEffectiveDate(EFFECTIVE_DATE.toString());
        request.setSalary(SALARY);

        when(compensationRepository.findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE))
                .thenReturn(new com.mindex.challenge.data.Compensation(EMPLOYEE_ID, SALARY, EFFECTIVE_DATE));

        // when/then
        assertThrows(DuplicateEntityException.class, () -> {
            compensationService.create(request, EMPLOYEE_ID);
        });

        verify(compensationRepository).findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);
        verify(compensationRepository, never()).insert((any(Compensation.class)));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testReadByEffectiveDate_CompensationNotFound_ThrowsException() {
        // given
        when(compensationRepository.findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE)).thenReturn(null);

        // when
        compensationService.readByEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);

        // then
        verify(compensationRepository).findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);
    }

    @Test(expected = UnexpectedDatabaseException.class)
    public void testReadAll_DataAccessException_ThrowsException() {
        // given
        when(compensationRepository.findByEmployeeId(EMPLOYEE_ID)).thenThrow(new DataAccessException("Database error") {});

        // when
        compensationService.readAll(EMPLOYEE_ID);

        // then
        verify(compensationRepository).findByEmployeeId(EMPLOYEE_ID);
    }

    @Test(expected = UnexpectedDatabaseException.class)
    public void testCreate_DataAccessExceptionDuringInsert_ThrowsException() {
        // given
        CompensationCreateRequest request = new CompensationCreateRequest();
        request.setEffectiveDate(EFFECTIVE_DATE.toString());
        request.setSalary(SALARY);

        when(compensationRepository.findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE)).thenReturn(null);
        when(compensationRepository.insert(any(Compensation.class))).thenThrow(new DataAccessException("Insert failed") {});

        // when
        compensationService.create(request, EMPLOYEE_ID);

        // then
        verify(compensationRepository).findByEmployeeIdAndEffectiveDate(EMPLOYEE_ID, EFFECTIVE_DATE);
        verify(compensationRepository).insert(any(Compensation.class));
    }
}
