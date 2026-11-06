
package com.hirelens.hirelens_backend.service;

import com.hirelens.hirelens_backend.entity.Resume;
import com.hirelens.hirelens_backend.repository.ResumeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeExtractionService extractionService;

    public ResumeService(
            ResumeRepository resumeRepository,
            ResumeExtractionService extractionService) {
        this.resumeRepository = resumeRepository;
        this.extractionService = extractionService;
    }

    @Transactional
    public Resume uploadResume(MultipartFile file) throws IOException {

        String extractedText = extractionService.extractText(file);

        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setFileType("application/pdf");
        resume.setExtractedText(extractedText);

        return resumeRepository.save(resume);
    }
}
