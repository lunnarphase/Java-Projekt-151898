package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.entity.JobOffer;
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
    @Operation(summary = "Get job offer by ID", description = "Retrieve a job offer by its ID")
    public JobOffer getJobOfferById(
            @Parameter(description = "ID of the job offer", required = true)
            @PathVariable Long id) {
        return jobOfferService.getJobOfferById(id);
    }

    @GetMapping
    @Operation(summary = "Get all job offers", description = "Retrieve a list of all job offers in the database")
    public List<JobOffer> getAllJobOffers() {
        return jobOfferService.getAllJobOffers();
    }

    @PostMapping
    @Operation(summary = "Create a new job offer", description = "Creates a new job offer in the database")
    @PreAuthorize("hasRole('HR')")
    public JobOffer createJobOffer(
            @Parameter(description = "JobOffer to be created", required = true)
            @RequestBody JobOffer jobOffer) {
        return jobOfferService.createJobOffer(jobOffer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing job offer", description = "Updates an existing job offer by its ID")
    public JobOffer updateJobOffer(
            @Parameter(description = "ID of the job offer to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated job offer data", required = true)
            @RequestBody JobOffer updatedJobOffer) {
        return jobOfferService.updateJobOffer(id, updatedJobOffer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job offer by ID", description = "Deletes the job offer with the specified ID")
    public void deleteJobOffer(
            @Parameter(description = "ID of the job offer to be deleted", required = true)
            @PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
    }
}