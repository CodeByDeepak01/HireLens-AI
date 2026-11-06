
package com.hirelens.hirelens_backend.controller;

import com.hirelens.hirelens_backend.entity.Resume;
import com.hirelens.hirelens_backend.repository.ResumeRepository;
import com.hirelens.hirelens_backend.service.ResumeAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
public class ResumeAnalysisController {

    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalysisController(
            ResumeRepository resumeRepository,
            ResumeAnalysisService resumeAnalysisService) {
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/{id}/ai-analysis")
    public ResponseEntity<?> analyzeResume(@PathVariable Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElse(null);

        if (resume == null) {
            return ResponseEntity.notFound().build();
        }

        System.out.println("EXTRACTED RESUME TEXT:");
        System.out.println(resume.getExtractedText());

        String result = resumeAnalysisService
                .extractResumeInformation(resume.getExtractedText());

        return ResponseEntity.ok(Map.of(
                "resumeId", resume.getId(),
                "fileName", resume.getFileName(),
                "aiAnalysis", result
        ));
    }
}
