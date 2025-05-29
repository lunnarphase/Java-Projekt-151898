package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.entity.Candidate;
import org.example.javaprojekt151898.service.CandidateService;
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
    @Operation(summary = "Get candidate by ID", description = "Retrieve a candidate by its ID")
    public Candidate getCandidateById(
            @Parameter(description = "ID of the candidate", required = true)
            @PathVariable Long id) {
        return candidateService.getCandidateById(id);
    }

    @GetMapping
    @Operation(summary = "Get all candidates", description = "Retrieve a list of all candidates")
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @PostMapping
    @Operation(summary = "Create a new candidate", description = "Creates a new candidate in the database")
    public Candidate createCandidate(
            @Parameter(description = "Candidate to be created", required = true)
            @RequestBody Candidate candidate) {
        return candidateService.createCandidate(candidate);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing candidate", description = "Updates an existing candidate by its ID")
    public Candidate updateCandidate(
            @Parameter(description = "ID of the candidate to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated candidate data", required = true)
            @RequestBody Candidate updatedCandidate) {
        return candidateService.updateCandidate(id, updatedCandidate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete candidate by ID", description = "Deletes the candidate with the specified ID")
    public void deleteCandidate(
            @Parameter(description = "ID of the candidate to be deleted", required = true)
            @PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }
}