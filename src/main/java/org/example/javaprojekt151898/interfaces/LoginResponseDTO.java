package org.example.javaprojekt151898.interfaces;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String accessToken;

    public String getToken() {
        return accessToken;
    }

    public void setToken(String token) {
        this.accessToken = token;
    }
}