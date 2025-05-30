package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.UserRole;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String loginEmail;
    private UserRole role;
}