package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Cotisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotisationRepository extends JpaRepository<Cotisation, Long> {
    List<Cotisation> findByAdherentId(Long adherentId);

}
