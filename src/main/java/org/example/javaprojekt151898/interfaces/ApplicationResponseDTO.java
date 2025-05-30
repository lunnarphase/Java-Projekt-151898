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

    private Long candidateId;
    private Long jobOfferId;
}