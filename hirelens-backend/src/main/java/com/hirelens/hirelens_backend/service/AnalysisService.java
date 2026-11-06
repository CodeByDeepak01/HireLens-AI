
package com.hirelens.hirelens_backend.service;

import com.hirelens.hirelens_backend.entity.Analysis;
import com.hirelens.hirelens_backend.entity.Job;
import com.hirelens.hirelens_backend.entity.Resume;
import com.hirelens.hirelens_backend.repository.AnalysisRepository;
import com.hirelens.hirelens_backend.repository.JobRepository;
import com.hirelens.hirelens_backend.repository.ResumeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final SemanticSimilarityService semanticSimilarityService;

    public AnalysisService(
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            JobRepository jobRepository,
            SemanticSimilarityService semanticSimilarityService) {

        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.semanticSimilarityService = semanticSimilarityService;
    }

    private static final List<String> SKILLS = Arrays.asList(
            "java",
            "spring boot",
            "spring",
            "sql",
            "mysql",
            "postgresql",
            "mongodb",
            "react",
            "javascript",
            "typescript",
            "html",
            "css",
            "python",
            "rest api",
            "git",
            "github",
            "docker",
            "aws",
            "data structures",
            "algorithms",
            "problem solving",
            "hibernate",
            "jpa",
            "maven",
            "microservices",
            "kubernetes",
            "redis",
            "linux",
            "c++",
            "c programming"
    );

    @Transactional
    public Analysis analyzeResume(Long resumeId, Long jobId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Resume not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Job not found"));

        String resumeText = normalize(resume.getExtractedText());
        String jobText = normalize(job.getJobDescription());

        if (resumeText.isBlank()) {
            throw new IllegalArgumentException(
                    "Resume has no extractable text");
        }

        if (jobText.isBlank()) {
            throw new IllegalArgumentException(
                    "Job description cannot be empty");
        }

        // Step 1: Extract required skills from job description
        List<String> requiredSkills = SKILLS.stream()
                .filter(skill -> containsSkill(jobText, skill))
                .collect(Collectors.toList());

        // Step 2: Identify matched skills
        List<String> matchedSkills = requiredSkills.stream()
                .filter(skill -> containsSkill(resumeText, skill))
                .collect(Collectors.toList());

        // Step 3: Identify missing skills
        List<String> missingSkills = requiredSkills.stream()
                .filter(skill -> !containsSkill(resumeText, skill))
                .collect(Collectors.toList());

        // Step 4: Calculate keyword score
        double keywordScore = requiredSkills.isEmpty()
                ? 0.0
                : matchedSkills.size() * 100.0
                / requiredSkills.size();

        // Step 5: Calculate semantic similarity using MiniLM
        double semanticScore =
                semanticSimilarityService.calculateSimilarity(
                        resumeText,
                        jobText
                );

        // Step 6: Calculate hybrid ATS score
        double hybridScore =
                (keywordScore * 0.50)
                        + (semanticScore * 0.50);

        hybridScore = Math.round(hybridScore * 100.0) / 100.0;

        // Step 7: Generate recommendations
        String recommendations;

        if (requiredSkills.isEmpty()) {

            recommendations =
                    "No supported skills were detected in the job description. "
                            + "Review the technical requirements manually. "
                            + "The semantic score is based on text similarity.";

        } else if (missingSkills.isEmpty()) {

            recommendations =
                    "All skills detected by the keyword matcher were found "
                            + "in the resume. Review the resume manually to "
                            + "verify evidence, experience and relevance.";

        } else {

            recommendations =
                    "Consider adding relevant evidence for these skills "
                            + "if you genuinely possess them: "
                            + String.join(", ", missingSkills);
        }

        // Step 8: Save analysis to PostgreSQL
        Analysis analysis = new Analysis();

        analysis.setResume(resume);
        analysis.setJob(job);
        analysis.setAtsScore(hybridScore);

        analysis.setMatchedSkills(
                String.join(", ", matchedSkills)
        );

        analysis.setMissingSkills(
                String.join(", ", missingSkills)
        );

        analysis.setRecommendations(recommendations);

        return analysisRepository.save(analysis);
    }

    public Analysis getAnalysisById(Long analysisId) {

        return analysisRepository.findById(analysisId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Analysis not found"));
    }

    private String normalize(String text) {

        return text == null
                ? ""
                : text.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean containsSkill(String text, String skill) {

        String pattern = "(?<![a-z0-9+#.])"
                + java.util.regex.Pattern.quote(skill)
                + "(?![a-z0-9+#.])";

        return text.matches("(?s).*" + pattern + ".*");
    }
}
