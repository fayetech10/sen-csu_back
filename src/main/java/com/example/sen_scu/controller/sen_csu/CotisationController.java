package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.model.sen_csu.Cotisation;
import com.example.sen_scu.service.sen_csu.CotisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/cotisation")
public class CotisationController {
    private final CotisationService cotisationService;

    @GetMapping("/adherent/{id}")
    public ResponseEntity<List<Cotisation>> getCotisations(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                cotisationService.getCotisationsByAdherent(id)
        );
    }

}
