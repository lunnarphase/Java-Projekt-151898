package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.Application;
import org.example.javaprojekt151898.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application with ID " + id + " not found."));
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application createApplication(Application application) {
        // W tym miejscu możesz np. automatycznie ustawiać status APPLIED,
        // lub walidować, czy jobOffer jest aktywny.
        return applicationRepository.save(application);
    }

    public Application updateApplication(Long id, Application updatedApplication) {
        Application existing = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application with ID " + id + " not found."));

        existing.setStatus(updatedApplication.getStatus());
        existing.setNotes(updatedApplication.getNotes());
        // Pola candidate, jobOffer – w zależności od tego, czy chcesz je modyfikować
        return applicationRepository.save(existing);
    }

    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Application with ID " + id + " does not exist.");
        }
        applicationRepository.deleteById(id);
    }
}