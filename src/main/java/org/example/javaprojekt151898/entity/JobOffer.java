package org.example.javaprojekt151898.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Lob // Dla dłuższych tekstów, można też użyć @Column(columnDefinition="TEXT")
    private String description;

    @Lob
    private String requirements;

    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String companyName; // Placeholder, do rozbudowy w przyszłości

    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;

    // Relacja do User (ManyToOne, rekruter) - do dodania później
    // @ManyToOne
    // @JoinColumn(name = "recruiter_id", referencedColumnName = "id")
    // private User recruiter;

    // Relacja do Application (OneToMany) - do dodania później
    // @OneToMany(mappedBy = "jobOffer")
    // private List<Application> applications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
