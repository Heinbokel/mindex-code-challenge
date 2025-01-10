package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface defining contract required for compensation repositories.
 * Compensation repositories handle managing data for employee compensation.
 *
 * @author Robert Heinbokel
 */
public interface ICompensationRepository extends MongoRepository<Compensation, String> {
    /**
     * Retrieves all compensation entries for a given employee.
     * @param employeeId the ID of the employee to retrieve compensation for.
     * @return the {@link List<Compensation>} to return.
     */
    List<Compensation> findByEmployeeId(String employeeId);

    /**
     * Retrieves the compensation entry for a given employee and effective date.
     * @param employeeId the ID of the employee to retrieve compensation for.
     * @param effectiveDate the effective date of the compensation.
     * @return the {@link Compensation} to return.
     */
    Compensation findByEmployeeIdAndEffectiveDate(String employeeId, LocalDate effectiveDate);
}
