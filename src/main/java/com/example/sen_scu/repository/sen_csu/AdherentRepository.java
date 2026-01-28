package com.example.sen_scu.repository.sen_csu;


import com.example.sen_scu.model.sen_csu.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    boolean existsBynumeroCNi(String numeroCNi);
    Adherent findAdherentByWhatsapp(String whatsapp);
    List<Adherent> findAllByAgentId(Long agentId);
    boolean existsByClientUUID(String clientUUID);

}
