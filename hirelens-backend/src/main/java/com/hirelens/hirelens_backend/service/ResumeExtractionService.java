
package com.hirelens.hirelens_backend.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeExtractionService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public String extractText(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please upload a PDF file");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "File size must not exceed 5 MB");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null
                || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException(
                    "Only PDF files are supported");
        }

        byte[] fileBytes = file.getBytes();

        if (fileBytes.length < 5
                || fileBytes[0] != '%'
                || fileBytes[1] != 'P'
                || fileBytes[2] != 'D'
                || fileBytes[3] != 'F'
                || fileBytes[4] != '-') {
            throw new IllegalArgumentException(
                    "Invalid PDF file");
        }

        try (PDDocument document = Loader.loadPDF(fileBytes)) {

            if (document.isEncrypted()) {
                throw new IllegalArgumentException(
                        "Password-protected PDFs are not supported");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document).trim();

            if (extractedText.isBlank()) {
                throw new IllegalArgumentException(
                        "No readable text found. Scanned PDFs require OCR.");
            }

            return extractedText;
        }
    }
}
