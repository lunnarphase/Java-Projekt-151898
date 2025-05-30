package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.interfaces.JobOfferRequestDTO;
import org.example.javaprojekt151898.interfaces.JobOfferResponseDTO;
import org.example.javaprojekt151898.service.JobOfferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
@Tag(name = "JobOffers", description = "Operations related to job offers")
public class JobOfferController {
    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "ALL - Get job offer by ID", description = "Retrieve a job offer by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public JobOfferResponseDTO getJobOfferById(
            @Parameter(description = "ID of the job offer", required = true)
            @PathVariable Long id) {
        return jobOfferService.getJobOfferById(id);
    }

    @GetMapping
    @Operation(summary = "ALL - Get all job offers", description = "Retrieve a list of all job offers in the database")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public List<JobOfferResponseDTO> getAllJobOffers() {
        return jobOfferService.getAllJobOffers();
    }

    @PostMapping
    @Operation(summary = "HR - Create a new job offer", description = "Creates a new job offer in the database")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public JobOfferResponseDTO createJobOffer(
            @Parameter(description = "JobOffer to be created", required = true)
            @RequestBody JobOfferRequestDTO jobOfferRequestDTO) {
        return jobOfferService.createJobOffer(jobOfferRequestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "HR - Update existing job offer", description = "Updates an existing job offer by its ID")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public JobOfferResponseDTO updateJobOffer(
            @Parameter(description = "ID of the job offer to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated job offer data", required = true)
            @RequestBody JobOfferRequestDTO updatedJobOfferDTO) {
        return jobOfferService.updateJobOffer(id, updatedJobOfferDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "HR - Delete job offer by ID", description = "Deletes the job offer with the specified ID")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public void deleteJobOffer(
            @Parameter(description = "ID of the job offer to be deleted", required = true)
            @PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
    }
}
