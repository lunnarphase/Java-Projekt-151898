package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.Application;
import org.example.javaprojekt151898.entity.ApplicationStatus;
import org.example.javaprojekt151898.interfaces.ApplicationRequestDTO;
import org.example.javaprojekt151898.interfaces.ApplicationResponseDTO;
import org.example.javaprojekt151898.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public ApplicationResponseDTO getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application with ID " + id + " not found."));
        return convertToResponseDTO(application);
    }

    public List<ApplicationResponseDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ApplicationResponseDTO createApplication(ApplicationRequestDTO applicationRequestDTO) {
        Application application = convertToEntity(applicationRequestDTO);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED); // Domyślny status

        Application savedApplication = applicationRepository.save(application);
        return convertToResponseDTO(savedApplication);
    }

    public ApplicationResponseDTO updateApplication(Long id, ApplicationRequestDTO updatedApplicationDTO) {
        Application existing = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application with ID " + id + " not found."));

        updateEntityFromDTO(existing, updatedApplicationDTO);
        Application updatedApplication = applicationRepository.save(existing);
        return convertToResponseDTO(updatedApplication);
    }

    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Application with ID " + id + " does not exist.");
        }
        applicationRepository.deleteById(id);
    }

    private ApplicationResponseDTO convertToResponseDTO(Application application) {
        ApplicationResponseDTO responseDTO = new ApplicationResponseDTO();
        responseDTO.setId(application.getId());
        responseDTO.setStatus(application.getStatus());
        responseDTO.setNotes(application.getNotes());
        responseDTO.setAppliedAt(application.getAppliedAt());

        if (application.getCandidate() != null) {
            responseDTO.setCandidateId(application.getCandidate().getId());
        }
        if (application.getJobOffer() != null) {
            responseDTO.setJobOfferId(application.getJobOffer().getId());
        }

        return responseDTO;
    }

    private Application convertToEntity(ApplicationRequestDTO requestDTO) {
        Application application = new Application();
        application.setStatus(requestDTO.getStatus());
        application.setNotes(requestDTO.getNotes());
        return application;
    }

    private void updateEntityFromDTO(Application application, ApplicationRequestDTO requestDTO) {
        if (requestDTO.getStatus() != null) {
            application.setStatus(requestDTO.getStatus());
        }
        if (requestDTO.getNotes() != null) {
            application.setNotes(requestDTO.getNotes());
        }
    }
}
