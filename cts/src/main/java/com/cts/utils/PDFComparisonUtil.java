package com.cts.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PDFComparisonUtil {

    public String extractText(String pdfPath) throws Exception {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    public Path getFirstPdfFile() {
        try {
            return Files.list(Path.of("src/test/resources/downloads"))
                    .filter(file -> file.toString().toLowerCase().endsWith(".pdf"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No PDF files found in downloads directory"));
        } catch (IOException e) {
            throw new RuntimeException("Error accessing downloads directory", e);
        }
    }

    public boolean verifyPDFDataAvailableInFile(List<String> dataToCompare) {
        String pdfData = null;
        boolean availableFlag = false;
        try {
            pdfData = extractText(getFirstPdfFile().toAbsolutePath().toString());
        } catch (Exception e) {
            System.out.println("Error reading downloaded file");
        }
        for (String eachData: dataToCompare) {
            if (eachData.split(" ").length > 1) {
                String[] splittedText = eachData.split(" ");
                availableFlag = pdfData.contains(splittedText[0]) && pdfData.contains(splittedText[1]);
            } else {
                if (pdfData.contains(eachData)) {
                    availableFlag= true;
                } else {
                    availableFlag = false;
                    break;
                }
            }
        }
        return availableFlag;
    }

    private double jaccardSimilarity(String text1, String text2) {
        Set<String> set1 = new HashSet<>(Set.of(text1.split("\\s+")));
        Set<String> set2 = new HashSet<>(Set.of(text2.split("\\s+")));
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return ((double) intersection.size() / union.size())*100 ;
    }

    public boolean verifyDataContainsInPDF(String dataToCompare) {
        String pdfData = null;
        try {
            pdfData = extractText(getFirstPdfFile().toAbsolutePath().toString());
        } catch (Exception e) {
            System.out.println("Error reading downloaded file");
        }
        return jaccardSimilarity(pdfData, dataToCompare) > 0.0;
    }

}
