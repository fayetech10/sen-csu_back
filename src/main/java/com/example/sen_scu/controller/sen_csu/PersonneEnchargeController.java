package com.example.sen_scu.controller.sen_csu;


import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.service.sen_csu.PersonneEnchargeSevice;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
@RequestMapping("/api/personnes-charge")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PersonneEnchargeController {

    private final PersonneEnchargeSevice personneChargeService;

    // Endpoint pour créer une personne en charge
    @PostMapping("/create")
    public ResponseEntity<?> createPersonneCharge(
            @RequestBody PersonneChargeRequest request,
            @RequestParam Long adherentId
           ) {
        try {
            PersonneCharge saved = personneChargeService.savePersonneChargeRequest(request, adherentId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Personne en charge créée avec succès",
                    "data", Map.of("personneChargeId", saved.getId())
            ));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erreur : doublon ou violation d'intégrité"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erreur serveur : " + e.getMessage()
            ));


        }


    }
    @PostMapping("/adherents/{adherentId}/personnes-charge")
    public ResponseEntity<?> addPersonneCharge(
            @PathVariable Long adherentId,
            @RequestBody @Valid PersonneChargeRequest request
    ) {
        try {
            PersonneCharge created =
                    personneChargeService.savePersonneChargeRequest(request, adherentId);

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}

