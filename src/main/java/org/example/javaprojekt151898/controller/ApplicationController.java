package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.interfaces.ApplicationRequestDTO;
import org.example.javaprojekt151898.interfaces.ApplicationResponseDTO;
import org.example.javaprojekt151898.service.ApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "ALL - Get application by ID", description = "Retrieve an application by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public ApplicationResponseDTO getApplicationById(
            @Parameter(description = "ID of the application", required = true)
            @PathVariable Long id) {
        return applicationService.getApplicationById(id);
    }

    @GetMapping
    @Operation(summary = "HR / ADMIN - Get all applications", description = "Retrieve a list of all applications")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public List<ApplicationResponseDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PostMapping
    @Operation(summary = "CANDIDATE / ADMIN - Create a new application", description = "Creates a new application in the database")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'ADMIN')")
    public ApplicationResponseDTO createApplication(
            @Parameter(description = "Application to be created", required = true)
            @RequestBody ApplicationRequestDTO applicationRequestDTO) {
        return applicationService.createApplication(applicationRequestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "CANDIDATE / ADMIN - Update existing application", description = "Updates an existing application by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'ADMIN')")
    public ApplicationResponseDTO updateApplication(
            @Parameter(description = "ID of the application to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated application data", required = true)
            @RequestBody ApplicationRequestDTO updatedApplicationDTO) {
        return applicationService.updateApplication(id, updatedApplicationDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ADMIN - Delete application by ID", description = "Deletes the application with the specified ID")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteApplication(
            @Parameter(description = "ID of the application to be deleted", required = true)
            @PathVariable Long id) {
        applicationService.deleteApplication(id);
    }
}
