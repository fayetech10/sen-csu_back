package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.model.sen_csu.Cotisation;
import com.example.sen_scu.repository.sen_csu.CotisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CotisationServiceImpl implements CotisationService {
    private final CotisationRepository cotisationRepository;
    @Override
    public List<Cotisation> getCotisationsByAdherent(Long adherentId) {
        return cotisationRepository.findByAdherentId(adherentId);
    }
}
