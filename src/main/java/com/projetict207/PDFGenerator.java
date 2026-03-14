package com.projetict207;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public static String generatePV(String fileName, String ueCode, String ueNom, String filiere, String niveau, 
                                   String semestre, String anneeAcademique, String dateGeneration,
                                   List<DatabaseConnector.PVLine> lines, int effectifTotal, 
                                   int nbCa, int nbCant, int nbNc, int nbEl) throws IOException {
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
            PDType1Font fontRegular = PDType1Font.HELVETICA;
            
            PDPageContentStream content = new PDPageContentStream(document, page);
            
            float yPosition = 800;
            float margin = 50;
            float pageWidth = page.getMediaBox().getWidth();
            
            // Header - University name
            content.beginText();
            content.setFont(fontBold, 14);
            content.newLineAtOffset(margin, yPosition);
            content.showText("UNIVERSITÉ DE YAOUNDÉ I");
            content.endText();
            
            yPosition -= 20;
            content.beginText();
            content.setFont(fontBold, 12);
            content.newLineAtOffset(margin, yPosition);
            content.showText("FACULTÉ DES SCIENCES");
            content.endText();
            
            yPosition -= 30;
            
            // Title
            content.beginText();
            content.setFont(fontBold, 16);
            content.newLineAtOffset((pageWidth - 300) / 2, yPosition);
            content.showText("PROCÈS-VERBAL DE DELIBERATION");
            content.endText();
            
            yPosition -= 25;
            
            // Exam info
            content.beginText();
            content.setFont(fontRegular, 11);
            content.newLineAtOffset(margin, yPosition);
            content.showText("Unité d'Enseignement: " + ueCode + " - " + ueNom);
            content.endText();
            
            yPosition -= 15;
            content.beginText();
            content.setFont(fontRegular, 11);
            content.newLineAtOffset(margin, yPosition);
            content.showText("Filière: " + filiere + "    Niveau: " + niveau + "    Semestre: " + semestre + "    Année académique: " + anneeAcademique);
            content.endText();
            
            yPosition -= 15;
            content.beginText();
            content.setFont(fontRegular, 11);
            content.newLineAtOffset(margin, yPosition);
            content.showText("Date: " + dateGeneration);
            content.endText();
            
            yPosition -= 30;
            
            // Table header
            float[] colWidths = {60, 120, 40, 40, 40, 40, 40, 50};
            String[] headers = {"Matricule", "Nom et Prénoms", "CC", "TP", "EE", "EP", "Total", "Décision"};
            
            // Header background
            content.setNonStrokingColor(200, 200, 200);
            content.addRect(margin, yPosition - 5, pageWidth - 2 * margin, 18);
            content.fill();
            content.setNonStrokingColor(0, 0, 0);
            
            content.beginText();
            content.setFont(fontBold, 9);
            float xOffset = margin + 5;
            for (int i = 0; i < headers.length; i++) {
                content.newLineAtOffset(xOffset, yPosition);
                content.showText(headers[i]);
                xOffset += colWidths[i];
            }
            content.endText();
            
            yPosition -= 20;
            
            // Table rows
            content.setFont(fontRegular, 8);
            for (DatabaseConnector.PVLine line : lines) {
                if (yPosition < 100) {
                    break;
                }
                
                xOffset = margin + 5;
                content.beginText();
                content.newLineAtOffset(xOffset, yPosition);
                content.showText(line.matricule != null ? line.matricule : "");
                
                xOffset += colWidths[0];
                content.newLineAtOffset(xOffset - margin - 5, 0);
                String nomComplet = line.nomComplet != null ? line.nomComplet : "";
                if (nomComplet.length() > 25) nomComplet = nomComplet.substring(0, 25);
                content.showText(nomComplet);
                
                xOffset += colWidths[1];
                content.newLineAtOffset(xOffset - margin - 5 - colWidths[0], 0);
                content.showText(String.format("%.1f", line.cc));
                
                xOffset += colWidths[2];
                content.newLineAtOffset(colWidths[2], 0);
                content.showText(String.format("%.1f", line.tp));
                
                xOffset += colWidths[3];
                content.newLineAtOffset(colWidths[3], 0);
                content.showText(String.format("%.1f", line.ee));
                
                xOffset += colWidths[4];
                content.newLineAtOffset(colWidths[4], 0);
                content.showText(String.format("%.1f", line.ep));
                
                xOffset += colWidths[5];
                content.newLineAtOffset(colWidths[5], 0);
                content.showText(String.format("%.1f", line.total));
                
                xOffset += colWidths[6];
                content.newLineAtOffset(colWidths[6], 0);
                content.showText(line.decision);
                
                content.endText();
                yPosition -= 15;
            }
            
            yPosition -= 20;
            
            // Statistics
            content.setNonStrokingColor(240, 240, 240);
            content.addRect(margin, yPosition - 60, pageWidth - 2 * margin, 70);
            content.fill();
            content.setNonStrokingColor(0, 0, 0);
            
            content.beginText();
            content.setFont(fontBold, 11);
            content.newLineAtOffset(margin + 10, yPosition - 15);
            content.showText("STATISTIQUES:");
            content.endText();
            
            content.setFont(fontRegular, 10);
            double pctCa = effectifTotal > 0 ? nbCa * 100.0 / effectifTotal : 0;
            double pctCant = effectifTotal > 0 ? nbCant * 100.0 / effectifTotal : 0;
            double pctNc = effectifTotal > 0 ? nbNc * 100.0 / effectifTotal : 0;
            double pctEl = effectifTotal > 0 ? nbEl * 100.0 / effectifTotal : 0;
            
            content.beginText();
            content.newLineAtOffset(margin + 10, yPosition - 35);
            content.showText(String.format("Effectif total: %d", effectifTotal));
            content.endText();
            
            content.beginText();
            content.newLineAtOffset(margin + 150, yPosition - 35);
            content.showText(String.format("Capacité (CA): %d (%.1f%%)", nbCa, pctCa));
            content.endText();
            
            content.beginText();
            content.newLineAtOffset(margin + 300, yPosition - 35);
            content.showText(String.format("CANT: %d (%.1f%%)", nbCant, pctCant));
            content.endText();
            
            content.beginText();
            content.newLineAtOffset(margin + 10, yPosition - 50);
            content.showText(String.format("Non Capable (NC): %d (%.1f%%)", nbNc, pctNc));
            content.endText();
            
            content.beginText();
            content.newLineAtOffset(margin + 150, yPosition - 50);
            content.showText(String.format("Éliminé (EL): %d (%.1f%%)", nbEl, pctEl));
            content.endText();
            
            // Signature
            yPosition -= 100;
            content.beginText();
            content.setFont(fontRegular, 10);
            content.newLineAtOffset(margin, yPosition);
            content.showText("Signature du Président du Jury");
            content.endText();
            
            content.beginText();
            content.setFont(fontRegular, 10);
            content.newLineAtOffset(pageWidth - margin - 150, yPosition);
            content.showText("Signature du Responsable de la Filière");
            content.endText();
            
            content.close();
            
            // Save PDF
            document.save(fileName);
            
            return fileName;
        }
    }
}
