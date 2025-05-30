package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.Candidate;
import org.example.javaprojekt151898.interfaces.CandidateRequestDTO;
import org.example.javaprojekt151898.interfaces.CandidateResponseDTO;
import org.example.javaprojekt151898.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public CandidateResponseDTO getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate with ID " + id + " not found."));
        return convertToResponseDTO(candidate);
    }

    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CandidateResponseDTO> getCandidatesByJobOfferId(Long jobOfferId) {
        List<Candidate> candidates = candidateRepository.findByApplications_JobOffer_Id(jobOfferId);
        return candidates.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    public CandidateResponseDTO createCandidate(CandidateRequestDTO candidateRequestDTO) {
        Candidate candidate = convertToEntity(candidateRequestDTO);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return convertToResponseDTO(savedCandidate);
    }

    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO updatedCandidateDTO) {
        Candidate existing = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate with ID " + id + " not found."));

        updateEntityFromDTO(existing, updatedCandidateDTO);
        Candidate updatedCandidate = candidateRepository.save(existing);
        return convertToResponseDTO(updatedCandidate);
    }

    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("Candidate with ID " + id + " does not exist.");
        }
        candidateRepository.deleteById(id);
    }

    private CandidateResponseDTO convertToResponseDTO(Candidate candidate) {
        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(candidate.getId());
        responseDTO.setFirstName(candidate.getFirstName());
        responseDTO.setLastName(candidate.getLastName());
        responseDTO.setContactEmail(candidate.getContactEmail());
        responseDTO.setPhoneNumber(candidate.getPhoneNumber());
        responseDTO.setCvPath(candidate.getCvPath());
        responseDTO.setSkills(candidate.getSkills());
        if (candidate.getUser() != null) {
            responseDTO.setUserId(candidate.getUser().getId());
        }
        return responseDTO;
    }

    private Candidate convertToEntity(CandidateRequestDTO requestDTO) {
        Candidate candidate = new Candidate();
        candidate.setFirstName(requestDTO.getFirstName());
        candidate.setLastName(requestDTO.getLastName());
        candidate.setContactEmail(requestDTO.getContactEmail());
        candidate.setPhoneNumber(requestDTO.getPhoneNumber());
        candidate.setCvPath(requestDTO.getCvPath());
        candidate.setSkills(requestDTO.getSkills());
        return candidate;
    }

    private void updateEntityFromDTO(Candidate candidate, CandidateRequestDTO requestDTO) {
        candidate.setFirstName(requestDTO.getFirstName());
        candidate.setLastName(requestDTO.getLastName());
        candidate.setContactEmail(requestDTO.getContactEmail());
        candidate.setPhoneNumber(requestDTO.getPhoneNumber());
        candidate.setCvPath(requestDTO.getCvPath());
        candidate.setSkills(requestDTO.getSkills());
    }
}
