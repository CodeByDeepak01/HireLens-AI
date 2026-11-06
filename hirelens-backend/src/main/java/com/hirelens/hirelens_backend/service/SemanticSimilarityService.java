
package com.hirelens.hirelens_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class SemanticSimilarityService {

    private final RestClient restClient;

    public SemanticSimilarityService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8000")
                .build();
    }

    public double calculateSimilarity(
            String resumeText,
            String jobDescription) {

        Map<String, String> requestBody = Map.of(
                "resume_text", resumeText,
                "job_description", jobDescription
        );

        Map<String, Double> response = restClient.post()
                .uri("/similarity")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        if (response == null ||
                response.get("semantic_similarity_score") == null) {
            throw new RuntimeException(
                    "Failed to get semantic similarity score"
            );
        }

        return response.get("semantic_similarity_score");
    }
}
