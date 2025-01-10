package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.requests.CompensationCreateRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface defining the contract for compensation services.
 *
 * @author Robert Heinbokel
 */
public interface ICompensationService {
    /**
     * Creates a new compensation entry for the given employee.
     * @param request The {@link CompensationCreateRequest} containing the compensation details.
     * @param employeeId The employee ID linked to the compensation.
     * @return the created {@link Compensation} to return.
     */
    Compensation create(CompensationCreateRequest request, String employeeId);

    /**
     * Retrieves all compensations for a given employee.
     * @param employeeId the employee ID linked to the compensations.
     * @return the {@link List<Compensation>} to return.
     */
    List<Compensation> readAll(String employeeId);

    /**
     * Retrieves a specific compensation entry based on the criteria.
     * @param employeeId The employee ID linked to the compensation.
     * @param effectiveDate The effective date as a {@link LocalDate}.
     * @return the {@link Compensation} to return.
     */
    Compensation readByEffectiveDate(String employeeId, LocalDate effectiveDate);
}
