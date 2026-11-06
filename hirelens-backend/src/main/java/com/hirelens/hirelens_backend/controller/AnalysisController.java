
package com.hirelens.hirelens_backend.controller;

import com.hirelens.hirelens_backend.entity.Analysis;
import com.hirelens.hirelens_backend.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analyses")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping
    public ResponseEntity<?> analyzeResume(
            @RequestBody Map<String, Long> request) {

        Long resumeId = request.get("resumeId");
        Long jobId = request.get("jobId");

        if (resumeId == null || jobId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "resumeId and jobId are required"
                    ));
        }

        try {
            Analysis analysis =
                    analysisService.analyzeResume(resumeId, jobId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "id", analysis.getId(),
                            "resumeId", analysis.getResume().getId(),
                            "jobId", analysis.getJob().getId(),
                            "atsScore", analysis.getAtsScore(),
                            "matchedSkills", analysis.getMatchedSkills(),
                            "missingSkills", analysis.getMissingSkills(),
                            "recommendations", analysis.getRecommendations(),
                            "message", "Resume analysis completed successfully"
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getAnalysisById(@PathVariable Long id) {
        try {
            Analysis analysis = analysisService.getAnalysisById(id);

            return ResponseEntity.ok(
                    Map.of(
                            "id", analysis.getId(),
                            "resumeId", analysis.getResume().getId(),
                            "jobId", analysis.getJob().getId(),
                            "atsScore", analysis.getAtsScore(),
                            "matchedSkills", analysis.getMatchedSkills(),
                            "missingSkills", analysis.getMissingSkills(),
                            "recommendations", analysis.getRecommendations(),
                            "createdAt", analysis.getCreatedAt()
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
