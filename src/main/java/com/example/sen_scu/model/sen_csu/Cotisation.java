package com.example.sen_scu.model.sen_csu;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Cotisation {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate dateSoumission;

    @ManyToOne
    private Adherent adherent;
}
