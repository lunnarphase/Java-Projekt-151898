package org.example.javaprojekt151898.interfaces;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String message;

    private Long createdUserId;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}