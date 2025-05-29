package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.entity.Application;
import org.example.javaprojekt151898.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Operations related to applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID", description = "Retrieve an application by its ID")
    public Application getApplicationById(
            @Parameter(description = "ID of the application", required = true)
            @PathVariable Long id) {
        return applicationService.getApplicationById(id);
    }

    @GetMapping
    @Operation(summary = "Get all applications", description = "Retrieve a list of all applications")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PostMapping
    @Operation(summary = "Create a new application", description = "Creates a new application in the database")
    public Application createApplication(
            @Parameter(description = "Application to be created", required = true)
            @RequestBody Application application) {
        return applicationService.createApplication(application);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing application", description = "Updates an existing application by its ID")
    public Application updateApplication(
            @Parameter(description = "ID of the application to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated application data", required = true)
            @RequestBody Application updatedApplication) {
        return applicationService.updateApplication(id, updatedApplication);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete application by ID", description = "Deletes the application with the specified ID")
    public void deleteApplication(
            @Parameter(description = "ID of the application to be deleted", required = true)
            @PathVariable Long id) {
        applicationService.deleteApplication(id);
    }
}