package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.CotisationRequest;
import com.example.sen_scu.model.sen_csu.Cotisation;

import java.util.List;
import java.util.Optional;

public interface CotisationService {
    List<Cotisation> getCotisationsByAdherent(Long adherentId);
}
