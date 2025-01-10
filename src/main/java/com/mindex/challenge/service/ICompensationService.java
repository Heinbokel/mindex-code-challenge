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
    Compensation create(CompensationCreateRequest request, String employeeId);
    List<Compensation> readAll(String employeeId);
    Compensation readByEffectiveDate(String employeeId, LocalDate effectiveDate);
}
