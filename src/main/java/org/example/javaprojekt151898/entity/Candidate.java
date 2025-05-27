package org.example.javaprojekt151898.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private String cvPath; // Placeholder, do dyskusji jak przechowywać CV
    private String skills; // Placeholder, do rozbudowy w przyszłości

    // Relacja do User (np. OneToOne) - do dodania później
    // @OneToOne
    // @JoinColumn(name = "user_id", referencedColumnName = "id")
    // private User user;

    // Relacja do Application (OneToMany) - do dodania później
    // @OneToMany(mappedBy = "candidate")
    // private List<Application> applications;

    private LocalDateTime createdAt; // Możemy dodać automatyczne ustawianie przy tworzeniu
    private LocalDateTime updatedAt; // Możemy dodać automatyczne ustawianie przy aktualizacji
}
