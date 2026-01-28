package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.model.sen_csu.Cotisation;

import java.util.List;

public interface CotisationService {
    List<Cotisation> getCotisationsByAdherent(Long adherentId);
}
