package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.ApplicationStatus;

import java.time.LocalDateTime;

@Data
public class ApplicationResponseDTO {
    private Long id;
    private ApplicationStatus status;
    private String notes;
    private LocalDateTime appliedAt;
    // W odpowiedzi można zwrócić ID kandydata i ID oferty,
    // ewentualnie w formie uproszczonych obiektów (CandidateResponseDTO, JobOfferResponseDTO).
    private Long candidateId;
    private Long jobOfferId;
}