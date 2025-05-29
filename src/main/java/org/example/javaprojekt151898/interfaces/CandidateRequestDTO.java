package org.example.javaprojekt151898.interfaces;

import lombok.Data;

@Data
public class CandidateRequestDTO {
    private String firstName;
    private String lastName;
    private String contactEmail;
    private String phoneNumber;
    private String cvPath;
    private String skills;
    // Zwykle w requestach do tworzenia kandydata wystarczy userId,
    // żeby powiązać go z istniejącym userem.
    private Long userId;
}