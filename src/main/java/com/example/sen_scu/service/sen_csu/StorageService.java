package com.example.sen_scu.service.sen_csu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {
    @Value("${app.storage.path}")
    private String storagePath;
    public String saveFile(MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Fichier vide");
            }

            Files.createDirectories(Paths.get(storagePath));

            String original = file.getOriginalFilename();
            String ext = original.substring(original.lastIndexOf("."));
            String filename = UUID.randomUUID() + ext;

            Path filePath = Paths.get(storagePath, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filename;

        } catch (Exception e) {
            throw new RuntimeException("Impossible d'enregistrer le fichier : " + e.getMessage());
        }

    }
    public Resource loadFile(String filename) {
        try {
            Path path = Paths.get(storagePath).resolve(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) return resource;
            throw new RuntimeException("Fichier introuvable");
        } catch (Exception e) {
            throw new RuntimeException("Erreur récupération fichier : " + filename);
        }
    }
}
