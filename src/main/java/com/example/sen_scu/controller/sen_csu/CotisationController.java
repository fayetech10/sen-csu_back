package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.model.sen_csu.Cotisation;
import com.example.sen_scu.service.sen_csu.CotisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/cotisation")
public class CotisationController {
    private final CotisationService cotisationService;

    @GetMapping("/adherent/{id}")
    public ResponseEntity<List<Cotisation>> getCotisations(@PathVariable Long id) {
        List<Cotisation> cotisations = cotisationService.getCotisationsByAdherent(id);

        // Si la liste est nulle, on renvoie une liste vide pour éviter l'erreur Android
        if (cotisations == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(cotisations);
    }

}
