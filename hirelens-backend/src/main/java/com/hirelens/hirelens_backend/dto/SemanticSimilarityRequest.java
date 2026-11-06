
package com.hirelens.hirelens_backend.dto;

public record SemanticSimilarityRequest(
        String resumeText,
        String jobDescription
) {
}
