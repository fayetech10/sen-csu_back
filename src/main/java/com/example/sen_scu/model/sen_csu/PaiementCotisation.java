package com.example.sen_scu.model.sen_csu;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "paiement_cotisation",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "reference")
        }
)
public class PaiementCotisation {

    @Id
    @GeneratedValue
    private Long id;

    private Double montant;
    private LocalDateTime datePaiement = LocalDateTime.now();


    @Column(unique = true)
    private String reference;

    private String photoPaiement;
    private String modePaiement;


    @OneToOne(cascade = CascadeType.ALL)
    private Adherent adherent;

    @OneToOne(cascade = CascadeType.ALL)
    private Cotisation cotisation;
}

