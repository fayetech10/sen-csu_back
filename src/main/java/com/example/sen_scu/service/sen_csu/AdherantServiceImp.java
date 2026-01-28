package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.mapper.AdherentMapper;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import com.example.sen_scu.repository.sen_csu.PersonneChargeRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdherantServiceImp implements AdherentService {

    private final AdherentRepository adherentRepository;
    private final AgentRepository agentRepository;
    private final AdherentMapper mapper;
    private final PersonneChargeRepository personneChargeRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public Adherent saveWithDependants(AdherentRequest request, Long agentId) {

        try {
            log.info("📥 Réception nouvelle demande d'adhésion : {}", request);

            // ==========================
            // 🔍 VALIDATION DE LA REQUÊTE
            // ==========================
            if (request == null) {
                throw new AdherentException("Requête invalide (null).");
            }

            // ==========================
            // 🔍 RÉCUPÉRATION DE L'AGENT
            // ==========================
            Agent agent = agentRepository.findById(agentId)
                    .orElseThrow(() -> new AdherentException("Agent " + agentId + " pas trouvé"));

            // ==========================
            // 🔧 CRÉATION DE L'ADHÉRENT
            // ==========================
            Adherent adherent = createAdherentFromRequest(request);

            if (adherent == null) {
                throw new AdherentException("Impossible de créer l'adhérent : données invalides.");
            }

            adherent.setAgent(agent);

            // ==========================
            // 🔍 VALIDATION CNI
            // ==========================
            String cni = adherent.getNumeroCNi();

            if (cni == null || cni.trim().isEmpty()) {
                throw new AdherentException("Le numéro CNI est requis.");
            }

            cni = cni.replaceAll("\\s+", "");

            if (!cni.matches("\\d{13}")) {
                throw new AdherentException("Le numéro CNI doit contenir exactement 13 chiffres.");
            }

            adherent.setNumeroCNi(cni);

            // Vérification unicité
            if (adherentRepository.existsBynumeroCNi(cni)) {
                throw new AdherentException("Un adhérent avec ce numéro CNI existe déjà.");
            }


            List<PersonneCharge> dependants = request.getPersonnesCharge();

//            if (dependants == null || dependants.isEmpty()) {
//                throw new AdherentException("Il faut ajouter au moins une personne à charge.");
//            }

            // Initialisation si null
            if (adherent.getPersonnesCharge() == null) {
                adherent.setPersonnesCharge(new ArrayList<  >());
            }

            // Association correcte
            for (PersonneCharge pc : dependants) {
                if (pc == null) continue;
                pc.setAdherent(adherent);
                adherent.getPersonnesCharge().add(pc);
            }


            Double total =  (double) ((1 + dependants.size()) * 3500 + 1000);
            adherent.setMontantTotal(total);

            Adherent saved = adherentRepository.save(adherent);
            log.info("Adhérent créé avec succès : {}", saved);

            return saved;

        } catch (AdherentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur interne : {}", e.getMessage(), e);
            throw new AdherentException("Erreur lors de la création de l'adhérent : " + e.getMessage());
        }
    }

    @Override
    public List<Adherent> getAllAdherents() {
        return adherentRepository.findAll();
    }

    @Override
    public Optional<Adherent> loginAdherent(String whatsapp, String password) {
        return Optional.ofNullable(adherentRepository.findAdherentByWhatsapp(whatsapp))
                .filter(adherent -> passwordEncoder.matches(password, adherent.getPassword()));
    }

    @Override
    public void deleteAdherent(Long id) {
        adherentRepository.deleteById(id);
    }

    @Override
    public List<Adherent> getAllAdherentsByAgentId(Long agentId) {
        return adherentRepository.findAllByAgentId(agentId);
    }




    @Transactional
    public void syncAdherent(AdherentRequest request) {
        if (adherentRepository.existsByClientUUID(request.getClientUUID())) {
            return;
        }
        Adherent adherent = mapper.toEntity(request);
        adherentRepository.save(adherent);
    }

    @Override
    public Optional<Adherent> getAdherentById(Long id) {
        return adherentRepository.findById(id);
    }


    private Adherent createAdherentFromRequest(AdherentRequest request) {

        Adherent adherent = new Adherent();

        adherent.setPrenoms(request.getPrenoms());
        adherent.setNom(request.getNom());
        adherent.setAdresse(request.getAdresse());
        adherent.setLieuNaissance(request.getLieuNaissance());
        adherent.setSexe(request.getSexe());
        adherent.setDateNaissance(request.getDateNaissance());
        adherent.setSituationMatrimoniale(request.getSituationMatrimoniale());
        adherent.setWhatsapp(request.getWhatsapp());
        adherent.setSecteurActivite(request.getSecteurActivite());
        adherent.setNumeroCNi(request.getNumeroCNi());
        adherent.setRegion(request.getRegion());
        adherent.setClientUUID(request.getClientUUID());
        adherent.setRole("Adherant");
        adherent.setPassword(passwordEncoder.encode("acmu00"));
        adherent.setDepartement(request.getDepartement());
        adherent.setCommune(request.getCommune());
        adherent.setMontantTotal(request.getMontantTotal());
        adherent.setPhoto(request.getPhoto());
        adherent.setPhotoRecto(request.getPhotoRecto());
        adherent.setPhotoVerso(request.getPhotoVerso());

        return adherent;
    }


}
