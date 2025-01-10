package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.requests.CompensationCreateRequest;
import com.mindex.challenge.service.ICompensationService;
import com.mindex.challenge.validators.ValidUUID;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller handling requests related to employee compensation.
 *
 * @author Robert Heinbokel
 */
@RestController
@RequestMapping("/compensation")
@Validated
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    private final ICompensationService compensationService;

    /**
     * Constructor for dependency injection.
     * @param compensationService The {@link ICompensationService} to use.
     */
    public CompensationController(ICompensationService compensationService) {
        this.compensationService = compensationService;
    }

    /**
     * Retrieves all compensation entries for a given employee ID.
     * @param employeeId the ID of the employee to retrieve compensations for.
     * @return the {@link List<Compensation>} to return.
     */
    @GetMapping("/{employeeId}")
    public List<Compensation> readAll(@PathVariable @ValidUUID(message = "Employee ID must be a valid UUID") String employeeId) {
        LOG.debug("Received request to read all compensations for employeeId {}", employeeId);
        return compensationService.readAll(employeeId);
    }

    /**
     * Retrieves the compensation entry for a given employee ID and effective date.
     * @param employeeId the ID of the employee to retrieve compensations for.
     * @param effectiveDate the effective date of the compensation.
     * @return the {@link Compensation} to return.
     */
    @GetMapping("/{employeeId}/effective-date/{effectiveDate}")
    public Compensation readByEffectiveDate(@PathVariable @ValidUUID(message = "Employee ID must be a valid UUID") String employeeId, @PathVariable String effectiveDate) {
        LOG.debug("Received request to read compensation for employeeId {} and effectiveDate {}", employeeId, effectiveDate);
        return compensationService.readByEffectiveDate(employeeId, LocalDate.parse(effectiveDate));
    }

    /**
     * Creates a new entry for an employee's compensation.
     * @param request the {@link CompensationCreateRequest} holding the compensation details.
     * @param employeeId the ID of the employee this compensation belongs to.
     * @return the created {@link Compensation}.
     */
    @PostMapping("/{employeeId}")
    public Compensation create(@RequestBody @Valid CompensationCreateRequest request, @PathVariable @ValidUUID(message = "Employee ID must be a valid UUID") String employeeId) {
        LOG.debug("Received request to create compensation [{}] for employeeId {}", request, employeeId);
        return compensationService.create(request, employeeId);
    }
}
