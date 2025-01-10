package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.ICompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.requests.CompensationCreateRequest;
import com.mindex.challenge.exceptions.DuplicateEntityException;
import com.mindex.challenge.exceptions.ResourceNotFoundException;
import com.mindex.challenge.exceptions.UnexpectedDatabaseException;
import com.mindex.challenge.service.ICompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service implementation for managing employee compensation data.
 * Implements the {@link ICompensationService} interface.
 *
 * @author Robert Heinbokel
 */
@Service
public class CompensationServiceImpl implements ICompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    private final ICompensationRepository compensationRepository;

    /**
     * Constructor for dependency injection.
     * @param compensationRepository the {@link ICompensationRepository} to use.
     */
    public CompensationServiceImpl(ICompensationRepository compensationRepository) {
        this.compensationRepository = compensationRepository;
    }

    @Override
    public Compensation create(CompensationCreateRequest request, String employeeId) {
        LOG.debug("Creating compensation [{}] for employeeId [{}]", request, employeeId);

        // Could probably do this when deserializing but Strings can be nice to work with until you actually need a date.
        final LocalDate effectiveDate = LocalDate.parse(request.getEffectiveDate());

        // Ensure no duplicate compensation exists for the same employee and effective date.
        // For reviewers: Alternatively the _id in mongoDB would be a composite key of these values and error automatically.
        if (compensationRepository.findByEmployeeIdAndEffectiveDate(employeeId, effectiveDate) != null) {
            throw new DuplicateEntityException(String.format("Duplicate compensation found for employeeId: %s and effectiveDate: %s", employeeId, effectiveDate));
        }

        // Generate compensation to save
        final Compensation compensation = new Compensation(employeeId, request.getSalary(), effectiveDate);

        // Attempt to save the compensation.
        try {
            return compensationRepository.insert(compensation);
        } catch (DataAccessException ex) {
            throw new UnexpectedDatabaseException("Unable to create compensation for employeeId: " + employeeId, ex);
        }
    }

    @Override
    public List<Compensation> readAll(String employeeId) {
        LOG.debug("Reading all compensations for employeeId [{}]", employeeId);

        try {
            // returning empty collection is fine, wouldn't expect a 404.
            return compensationRepository.findByEmployeeId(employeeId);
        } catch (DataAccessException ex) {
            throw new UnexpectedDatabaseException("Unable to retrieve all compensations for employeeId: " + employeeId, ex);
        }
    }

    @Override
    public Compensation readByEffectiveDate(String employeeId, LocalDate effectiveDate) {
        LOG.debug("Reading compensation for employeeId [{}] and effectiveDate [{}]", employeeId, effectiveDate);

        try {
            final Compensation compensation = compensationRepository.findByEmployeeIdAndEffectiveDate(employeeId, effectiveDate);

            // returning null would be a strange response, so this bubbles up to a 404/error.
            if (compensation == null) {
                throw new ResourceNotFoundException(String.format("Compensation not found for employeeId: %s and effectiveDate: %s", employeeId, effectiveDate));
            }

            return compensation;
        } catch (DataAccessException ex) {
            throw new UnexpectedDatabaseException(String.format("Unable to retrieve compensation for employeeId: %s and effectiveDate: %s", employeeId, effectiveDate), ex);
        }
    }
}
