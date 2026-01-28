package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import com.example.sen_scu.repository.sen_csu.PersonneChargeRepository;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PersonneEnchargeSeviceImpl  implements PersonneEnchargeSevice {
    private final PersonneChargeRepository personneChargeRepository;
    private final AdherentRepository adherentRepository;
    private final AgentRepository agentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public PersonneCharge savePersonneChargeRequest(PersonneChargeRequest request, Long adherentId) {
// Récupérer l'adhérent
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new AdherentException("Adhérent non trouvé"));

        // Création de la personne en charge
        PersonneCharge personne = new PersonneCharge();
        personne.setPrenoms(request.getPrenoms());
        personne.setNom(request.getNom());
        personne.setSexe(request.getSexe());
        personne.setDateNaissance(request.getDateNaissance());
        personne.setLieuNaissance(request.getLieuNaissance());
        personne.setAdresse(request.getAdresse());
        personne.setWhatsapp(request.getTelephone());
        personne.setLienParent(request.getLienParent());
        personne.setPhoto(request.getPhoto());
        personne.setPhotoRecto(request.getPhotoRecto());
        personne.setPhotoVerso(request.getPhotoVerso());
        personne.setSituationM(request.getSituationM());
        personne.setNumeroCNi(request.getNumeroCNi());
        personne.setNumeroExtrait(request.getNumeroExtrait());

        // Associer les clés étrangères
        personne.setAdherent(adherent);


        // Sauvegarder en base
        return personneChargeRepository.save(personne);

    }
}
