package org.example.javaprojekt151898.interfaces;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String loginEmail;
    private String password;
}