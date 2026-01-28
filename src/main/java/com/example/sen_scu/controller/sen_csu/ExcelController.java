package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.service.sen_csu.AdherentService;
import com.example.sen_scu.service.sen_csu.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/excel")
public class ExcelController {


    private final AdherentService adherentService;

    private final ExcelService excelService;

    @GetMapping("/adherents")
    public ResponseEntity<InputStreamResource> downloadAllAdherents() throws IOException {

        List<Adherent> adherents = adherentService.getAllAdherents();

        // La bonne méthode !
        ByteArrayInputStream excel = excelService.adherentsToExcel(adherents);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=adherents.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }
}
