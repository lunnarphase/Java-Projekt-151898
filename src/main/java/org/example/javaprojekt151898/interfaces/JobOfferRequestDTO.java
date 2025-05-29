package org.example.javaprojekt151898.interfaces;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobOfferRequestDTO {
    private String title;
    private String description;
    private String requirements;
    private String location;
    private String companyName;
    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;
    private boolean isActive;

    // Jeżeli chcesz połączyć nowo tworzoną ofertę z użytkownikiem (rekruterem / HR):
    private Long createdById;
}