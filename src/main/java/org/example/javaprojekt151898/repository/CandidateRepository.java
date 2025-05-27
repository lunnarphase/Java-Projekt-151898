package org.example.javaprojekt151898.repository;

import org.example.javaprojekt151898.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
