package org.example.javaprojekt151898.interfaces;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobOfferResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String location;
    private String companyName;
    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;
    private boolean isActive;

    // Możesz zwrócić ID twórcy oferty, ewentualnie uproszczony obiekt usera:
    private Long createdById;
    // lub private UserResponseDTO createdBy; – w zależności od potrzeb
}