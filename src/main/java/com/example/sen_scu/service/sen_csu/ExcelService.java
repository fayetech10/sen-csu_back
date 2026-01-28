package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public ByteArrayInputStream adherentsToExcel(List<Adherent> adherents) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        // --- PRÉPARATION DES STYLES ---

        // Style pour l'en-tête (Gris foncé, Gras, Texte Blanc)
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Style Bleu Ciel pour les Adhérents
        CellStyle adherentStyle = workbook.createCellStyle();
        adherentStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        adherentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        adherentStyle.setBorderBottom(BorderStyle.THIN);
        adherentStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        // Style Blanc Standard pour les Personnes à Charge
        CellStyle pcStyle = workbook.createCellStyle();
        pcStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        pcStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // --- CRÉATION DES FEUILLES ---
        createDataSheet(workbook, "Liste SCU", adherents, headerStyle, adherentStyle, pcStyle);
        createDataSheet(workbook, "A IMPORTER", adherents, headerStyle, adherentStyle, pcStyle);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * Méthode générique pour remplir une feuille avec les données
     */
    private void createDataSheet(XSSFWorkbook workbook, String sheetName, List<Adherent> adherents,
                                 CellStyle headerStyle, CellStyle adhStyle, CellStyle pcStyle) {

        XSSFSheet sheet = workbook.createSheet(sheetName);

        // Définition des colonnes
        String[] columns = {
                "TYPE", "ID", "NOM", "PRENOM", "WHATSAPP / LIEN", "ADRESSE", "SEXE / DATE NAISS.",
                "REGIME / LIEU NAISS.", "DEPARTEMENT", "COMMUNE"
        };

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIdx = 1;
        for (Adherent adh : adherents) {
            // 1. Ligne Adhérent (Style Bleu Ciel)
            Row rowAdh = sheet.createRow(rowIdx++);
            fillAdherentRow(rowAdh, adh, adhStyle);

            // 2. Lignes Personnes à Charge (Style Blanc)
            List<PersonneCharge> charges = adh.getPersonnesCharge();
            if (charges != null) {
                for (PersonneCharge pc : charges) {
                    Row rowPc = sheet.createRow(rowIdx++);
                    fillPersonneChargeRow(rowPc, pc, pcStyle);
                }
            }
        }

        // Auto-ajustement des colonnes
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillAdherentRow(Row row, Adherent adh, CellStyle style) {
        String[] values = {
                "ADHERENT",
                String.valueOf(adh.getId()),
                adh.getNom(),
                adh.getPrenoms(),
                adh.getWhatsapp(),
                adh.getAdresse(),
                adh.getSexe(),
                adh.getRegime(),
                adh.getDepartement(),
                adh.getCommune()
        };

        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i] != null ? values[i] : "");
            cell.setCellStyle(style);
        }
    }

    private void fillPersonneChargeRow(Row row, PersonneCharge pc, CellStyle style) {
        // On laisse certaines colonnes vides ou on adapte le contenu pour les charges
        row.createCell(0).setCellValue("  ↳ CHARGE");
        row.createCell(1).setCellValue(pc.getId() != null ? pc.getId().toString() : "");
        row.createCell(2).setCellValue(pc.getNom());
        row.createCell(3).setCellValue(pc.getPrenoms());
        row.createCell(4).setCellValue(pc.getLienParent());
        row.createCell(5).setCellValue(""); // Pas d'adresse spécifique
        row.createCell(6).setCellValue(pc.getDateNaissance() != null ? pc.getDateNaissance().toString() : "");
        row.createCell(7).setCellValue(pc.getLieuNaissance());

        // Appliquer le style blanc à toutes les cellules de la ligne
        for (int i = 0; i < 10; i++) {
            if(row.getCell(i) == null) row.createCell(i);
            row.getCell(i).setCellStyle(style);
        }
    }
}