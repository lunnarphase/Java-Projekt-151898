package org.example.javaprojekt151898.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String notes;

    // Data złożenia aplikacji – ustawiana automatycznie - OnCreate()
    private LocalDateTime appliedAt;

    // Kandydat, który składa aplikację
    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;

    // Oferta, na którą aplikujemy
    @ManyToOne
    @JoinColumn(name = "job_offer_id", referencedColumnName = "id")
    private JobOffer jobOffer;

    // Metoda wykonywana przed zapisaniem encji do bazy
    @PrePersist
    protected void onCreate() {
        if (this.appliedAt == null) {
            this.appliedAt = LocalDateTime.now();
        }
    }
}