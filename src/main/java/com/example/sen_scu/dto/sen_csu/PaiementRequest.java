package com.example.sen_scu.dto.sen_csu;

import lombok.Data;

@Data
public class PaiementRequest {

    private Double montant;
    private String reference;
    private String photoPaiement;
    private String modePaiement;
    private Long adherentId;
}
