package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.ApplicationStatus;

@Data
public class ApplicationRequestDTO {
    private ApplicationStatus status;
    private String notes;
    // Najczęściej w request tworzenia aplikacji wystarczy candidateId oraz jobOfferId:
    private Long candidateId;
    private Long jobOfferId;
}