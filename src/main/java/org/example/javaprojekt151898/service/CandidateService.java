package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.Candidate;
import org.example.javaprojekt151898.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate with ID " + id + " not found."));
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        Candidate existing = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate with ID " + id + " not found."));

        existing.setFirstName(updatedCandidate.getFirstName());
        existing.setLastName(updatedCandidate.getLastName());
        existing.setContactEmail(updatedCandidate.getContactEmail());
        existing.setPhoneNumber(updatedCandidate.getPhoneNumber());
        existing.setCvPath(updatedCandidate.getCvPath());
        existing.setSkills(updatedCandidate.getSkills());
        // Jeśli chcesz, możesz również ustawiać existing.setUser(...) – zależnie od logiki

        return candidateRepository.save(existing);
    }

    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("Candidate with ID " + id + " does not exist.");
        }
        candidateRepository.deleteById(id);
    }
}