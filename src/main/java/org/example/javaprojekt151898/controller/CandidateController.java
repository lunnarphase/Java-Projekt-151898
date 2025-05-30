package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.interfaces.CandidateRequestDTO;
import org.example.javaprojekt151898.interfaces.CandidateResponseDTO;
import org.example.javaprojekt151898.service.CandidateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@Tag(name = "Candidates", description = "Operations related to candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "ALL - Get candidate by ID", description = "Retrieve a candidate by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public CandidateResponseDTO getCandidateById(
            @Parameter(description = "ID of the candidate", required = true)
            @PathVariable Long id) {
        return candidateService.getCandidateById(id);
    }

    @GetMapping
    @Operation(summary = "ADMIN - Get all candidates from the database", description = "Retrieve a list of all candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/by-job-offer/{jobOfferId}")
    @Operation(summary = "HR / ADMIN - Get candidates by job offer ID",
            description = "Retrieve all candidates who applied to specific job offer")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public List<CandidateResponseDTO> getCandidatesByJobOfferId(
            @Parameter(description = "ID of the job offer", required = true)
            @PathVariable Long jobOfferId) {
        return candidateService.getCandidatesByJobOfferId(jobOfferId);
    }

    @PostMapping
    @Operation(summary = "ALL - Create a new candidate", description = "Creates a new candidate in the database")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public CandidateResponseDTO createCandidate(
            @Parameter(description = "Candidate to be created", required = true)
            @RequestBody CandidateRequestDTO candidateRequestDTO) {
        return candidateService.createCandidate(candidateRequestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "ALL - Update existing candidate", description = "Updates an existing candidate by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public CandidateResponseDTO updateCandidate(
            @Parameter(description = "ID of the candidate to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated candidate data", required = true)
            @RequestBody CandidateRequestDTO updatedCandidateDTO) {
        return candidateService.updateCandidate(id, updatedCandidateDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ADMIN - Delete candidate by ID", description = "Deletes the candidate with the specified ID")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCandidate(
            @Parameter(description = "ID of the candidate to be deleted", required = true)
            @PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }
}
