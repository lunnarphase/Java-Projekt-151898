package org.example.javaprojekt151898.interfaces;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String message;
    // Ewentualnie można zwracać jakieś dane utworzonego użytkownika, np. ID
    private Long createdUserId;
}