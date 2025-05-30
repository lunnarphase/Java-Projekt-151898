package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.UserRole;

@Data
public class RegisterRequestDTO {
    private String username;
    private String loginEmail;
    private String password;

    private UserRole role;
}