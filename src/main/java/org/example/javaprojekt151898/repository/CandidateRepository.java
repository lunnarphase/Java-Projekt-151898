package org.example.javaprojekt151898.repository;

import org.example.javaprojekt151898.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("SELECT DISTINCT c FROM Candidate c JOIN c.applications a WHERE a.jobOffer.id = :jobOfferId")
    List<Candidate> findByApplications_JobOffer_Id(Long jobOfferId);
}
