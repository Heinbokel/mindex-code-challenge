package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.ICompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.requests.CompensationCreateRequest;
import com.mindex.challenge.service.ICompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        LOG.debug("Creating compensation [{}] for employeeId {}", request, employeeId);

        final LocalDate effectiveDate = LocalDate.parse(request.getEffectiveDate());

        // Ensure no duplicate compensation exists for the same employee and effective date.
        // For reviewers: Alternatively the _id in mongoDB would be a composite key of these values.
        if (compensationRepository.findByEmployeeIdAndEffectiveDate(employeeId, effectiveDate) != null) {
            throw new IllegalArgumentException(String.format("Duplicate compensation found for employeeId: %s and effectiveDate: %s", employeeId, effectiveDate));
        }

        final Compensation compensation = new Compensation();
        compensation.setEmployeeId(employeeId);
        compensation.setEffectiveDate(effectiveDate);
        compensation.setSalary(request.getSalary());

        return compensationRepository.insert(compensation);
    }

    @Override
    public List<Compensation> readAll(String employeeId) {
        LOG.debug("Reading all compensations for employeeId {}", employeeId);
        return compensationRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Compensation readByEffectiveDate(String employeeId, LocalDate effectiveDate) {
        LOG.debug("Reading compensation for employeeId {} and effectiveDate {}", employeeId, effectiveDate);

        Compensation compensation = compensationRepository.findByEmployeeIdAndEffectiveDate(employeeId, effectiveDate);

        if (compensation == null) {
            throw new RuntimeException("No compensation found for employeeId: "
                    + employeeId + " and effectiveDate: " + effectiveDate);
        }

        return compensation;
    }
}
