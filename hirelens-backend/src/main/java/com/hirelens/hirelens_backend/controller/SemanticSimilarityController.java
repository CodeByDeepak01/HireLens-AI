
package com.hirelens.hirelens_backend.controller;

import com.hirelens.hirelens_backend.dto.SemanticSimilarityRequest;
import com.hirelens.hirelens_backend.dto.SemanticSimilarityResponse;
import com.hirelens.hirelens_backend.service.SemanticSimilarityService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ml")
public class SemanticSimilarityController {

    private final SemanticSimilarityService semanticSimilarityService;

    public SemanticSimilarityController(
            SemanticSimilarityService semanticSimilarityService) {
        this.semanticSimilarityService = semanticSimilarityService;
    }

    @PostMapping("/similarity")
    public SemanticSimilarityResponse calculateSimilarity(
            @RequestBody SemanticSimilarityRequest request) {

        double score = semanticSimilarityService.calculateSimilarity(
                request.resumeText(),
                request.jobDescription()
        );

        return new SemanticSimilarityResponse(score);
    }
}
