package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.PaiementRequest;
import com.example.sen_scu.model.sen_csu.PaiementCotisation;
import com.example.sen_scu.service.sen_csu.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {
    private final PaiementService paiementService;

    @PostMapping("/add")
    public ResponseEntity<PaiementCotisation> add(@RequestBody PaiementRequest request) {

        return ResponseEntity.ok(paiementService.addPaiement(request));
    }
    @GetMapping({"/adherent/{adherentId}"})
    public ResponseEntity<?> getAllPaiementsByAdherentId(@PathVariable Long adherentId) {
        return ResponseEntity.ok(paiementService.getAllPaiementsByAdherentId(adherentId));
    }
}
