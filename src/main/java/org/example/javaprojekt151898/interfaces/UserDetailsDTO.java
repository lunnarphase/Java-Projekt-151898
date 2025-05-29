package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.UserRole;

@Data
public class UserDetailsDTO {
    private Long id;
    private String username;
    private String loginEmail;
    private UserRole role;
}