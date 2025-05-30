package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.JobOffer;
import org.example.javaprojekt151898.interfaces.JobOfferRequestDTO;
import org.example.javaprojekt151898.interfaces.JobOfferResponseDTO;
import org.example.javaprojekt151898.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    public JobOfferResponseDTO getJobOfferById(Long id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobOffer with ID " + id + " not found."));
        return convertToResponseDTO(jobOffer);
    }

    public List<JobOfferResponseDTO> getAllJobOffers() {
        return jobOfferRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public JobOfferResponseDTO createJobOffer(JobOfferRequestDTO jobOfferRequestDTO) {
        JobOffer jobOffer = convertToEntity(jobOfferRequestDTO);
        JobOffer savedJobOffer = jobOfferRepository.save(jobOffer);
        return convertToResponseDTO(savedJobOffer);
    }

    public JobOfferResponseDTO updateJobOffer(Long id, JobOfferRequestDTO updatedJobOfferDTO) {
        JobOffer existing = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobOffer with ID " + id + " not found."));

        updateEntityFromDTO(existing, updatedJobOfferDTO);
        JobOffer updatedJobOffer = jobOfferRepository.save(existing);
        return convertToResponseDTO(updatedJobOffer);
    }

    public void deleteJobOffer(Long id) {
        if (!jobOfferRepository.existsById(id)) {
            throw new RuntimeException("JobOffer with ID " + id + " does not exist.");
        }
        jobOfferRepository.deleteById(id);
    }

    private JobOfferResponseDTO convertToResponseDTO(JobOffer jobOffer) {
        JobOfferResponseDTO responseDTO = new JobOfferResponseDTO();
        responseDTO.setId(jobOffer.getId());
        responseDTO.setTitle(jobOffer.getTitle());
        responseDTO.setDescription(jobOffer.getDescription());
        responseDTO.setRequirements(jobOffer.getRequirements());
        responseDTO.setLocation(jobOffer.getLocation());
        responseDTO.setCompanyName(jobOffer.getCompanyName());
        responseDTO.setPublishedAt(jobOffer.getPublishedAt());
        responseDTO.setExpiresAt(jobOffer.getExpiresAt());
        responseDTO.setActive(jobOffer.isActive());
        if (jobOffer.getCreatedBy() != null) {
            responseDTO.setCreatedById(jobOffer.getCreatedBy().getId());
        }
        return responseDTO;
    }

    private JobOffer convertToEntity(JobOfferRequestDTO requestDTO) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setTitle(requestDTO.getTitle());
        jobOffer.setDescription(requestDTO.getDescription());
        jobOffer.setRequirements(requestDTO.getRequirements());
        jobOffer.setLocation(requestDTO.getLocation());
        jobOffer.setCompanyName(requestDTO.getCompanyName());
        jobOffer.setPublishedAt(requestDTO.getPublishedAt());
        jobOffer.setExpiresAt(requestDTO.getExpiresAt());
        jobOffer.setActive(requestDTO.isActive());
        return jobOffer;
    }

    private void updateEntityFromDTO(JobOffer jobOffer, JobOfferRequestDTO requestDTO) {
        jobOffer.setTitle(requestDTO.getTitle());
        jobOffer.setDescription(requestDTO.getDescription());
        jobOffer.setRequirements(requestDTO.getRequirements());
        jobOffer.setLocation(requestDTO.getLocation());
        jobOffer.setCompanyName(requestDTO.getCompanyName());
        jobOffer.setPublishedAt(requestDTO.getPublishedAt());
        jobOffer.setExpiresAt(requestDTO.getExpiresAt());
        jobOffer.setActive(requestDTO.isActive());
    }
}
