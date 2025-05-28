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
    // private BigDecimal salaryMin;
    // private BigDecimal salaryMax;
    private String companyName; // Placeholder, do rozbudowy w przyszłości

    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;
}
