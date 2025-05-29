package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.UserRole;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String loginEmail;
    private UserRole role;
// Możesz dodać ewentualnie listę JobOfferRequestDTO,
// ale zwykle w rejestracji użytkownika nie tworzy się od razu ofert.
// private List<JobOfferRequestDTO> jobOffers;
}