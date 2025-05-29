package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.JobOffer;
import org.example.javaprojekt151898.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    public JobOffer getJobOfferById(Long id) {
        return jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobOffer with ID " + id + " not found."));
    }

    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public JobOffer createJobOffer(JobOffer jobOffer) {
        // Możesz uzupełnić np. publishedAt = LocalDateTime.now(), isActive = true itd.
        return jobOfferRepository.save(jobOffer);
    }

    public JobOffer updateJobOffer(Long id, JobOffer updatedJobOffer) {
        JobOffer existing = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobOffer with ID " + id + " not found."));

        existing.setTitle(updatedJobOffer.getTitle());
        existing.setDescription(updatedJobOffer.getDescription());
        existing.setRequirements(updatedJobOffer.getRequirements());
        existing.setLocation(updatedJobOffer.getLocation());
        existing.setCompanyName(updatedJobOffer.getCompanyName());
        existing.setPublishedAt(updatedJobOffer.getPublishedAt());
        existing.setExpiresAt(updatedJobOffer.getExpiresAt());
        existing.setActive(updatedJobOffer.isActive());

        return jobOfferRepository.save(existing);
    }

    public void deleteJobOffer(Long id) {
        if (!jobOfferRepository.existsById(id)) {
            throw new RuntimeException("JobOffer with ID " + id + " does not exist.");
        }
        jobOfferRepository.deleteById(id);
    }
}