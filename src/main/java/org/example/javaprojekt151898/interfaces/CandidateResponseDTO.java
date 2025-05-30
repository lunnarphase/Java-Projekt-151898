package org.example.javaprojekt151898.interfaces;

import lombok.Data;

@Data
public class CandidateResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String contactEmail;
    private String phoneNumber;
    private String cvPath;
    private String skills;

    private Long userId;
}