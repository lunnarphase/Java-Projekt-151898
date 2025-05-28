package org.example.javaprojekt151898.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// Na razie pomijamy importy dla User i Application, dodamy je później przy definicji relacji

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_offers")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    @Lob
    private String requirements;

    private String location;
    private String companyName;

    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;

    private boolean isActive;

    // Twórca oferty (rekruter/HR)
    @ManyToOne // Jeden rekruter może tworzyć wiele ofert pracy
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    // Relacja odwrotna do Application (lista aplikacji na tę ofertę)
    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}
