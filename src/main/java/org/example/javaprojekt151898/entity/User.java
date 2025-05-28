package org.example.javaprojekt151898.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "login_email", unique = true, nullable = false)
    private String loginEmail;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Encja User jest właścicielem kolekcji JobOffer poprzez mappedBy = "createdBy"
    // CascadeType.ALL – usunięcie Usera spowoduje usunięcie wszystkich ofert pracy, które utworzył.
    // orphanRemoval = true – jeśli usuniemy z kolekcji jobOffers konkretną ofertę
    // (bez przypisania do innego Usera), to ta oferta również zostanie usunięta z bazy.
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobOffer> createdJobOffers = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Candidate candidate;
}
