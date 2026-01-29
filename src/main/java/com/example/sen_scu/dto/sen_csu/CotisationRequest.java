package com.example.sen_scu.dto.sen_csu;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CotisationRequest {

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double montant;
    private LocalDate dateSoumission;
    private Long adherentId;
}
