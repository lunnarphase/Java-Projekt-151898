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


    private Long createdById;
}