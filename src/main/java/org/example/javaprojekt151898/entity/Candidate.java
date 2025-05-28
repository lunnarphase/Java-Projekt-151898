package org.example.javaprojekt151898.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "contact_email", unique = true, nullable = false)
    private String contactEmail;

    private String phoneNumber;
    private String cvPath; // Placeholder, do dyskusji jak przechowywać CV
    private String skills; // Placeholder, do rozbudowy w przyszłości

    // Powiązanie z Userem – jeśli dany user jest kandydatem
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Relacja odwrotna do Application – jeden kandydat może mieć wiele aplikacji
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}
