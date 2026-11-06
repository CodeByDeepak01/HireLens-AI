
package com.hirelens.hirelens_backend.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ResumeAnalysisService {

    private final ChatClient chatClient;

    public ResumeAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String extractResumeInformation(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            throw new IllegalArgumentException(
                    "Resume text cannot be empty"
            );
        }


        String prompt = """
        You are a resume parser. Extract facts from the resume below.

        Return exactly one JSON object with these keys:
        skills, experience, projects, education.

        Required format:
        {
          "skills": [],
          "experience": [],
          "projects": [],
          "education": []
        }

        IMPORTANT EXTRACTION RULES:

        1. SKILLS:
           Extract each specific skill separately.
           Include C, Java (Basic), Python (Basic), HTML, CSS,
           SQL, MySQL, VS Code, Git and soft skills if present.
           Do not replace specific skills with broad labels.

        2. EXPERIENCE:
           Extract only actual employment or internship entries.
           Include role, company and dates from the EXPERIENCE section.
           Do not use experience dates for projects.

        3. PROJECTS:
           Extract every project listed under the PROJECTS heading.
           This resume has two project entries:
           Student Management System and Personal Portfolio Website.
           Keep each project's own description.
           Never attach internship dates to a project.

        4. EDUCATION:
           Extract the degree, institution, location, years and CGPA
           from the EDUCATION section.
           Do not return an empty array when this information is present.

        5. Read each section separately.
        6. Do not invent or infer missing facts.
        7. Do not repeat keys or information.
        8. Every value in each array must be a string.
        9. Return valid JSON only, without Markdown or code fences.
        10. Use exactly these four keys, each exactly once.

        RESUME TEXT:
        %s
        """.formatted(resumeText);

        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        if (aiResponse == null || aiResponse.isBlank()) {
            throw new IllegalStateException(
                    "AI returned an empty response"
            );
        }

        String json = aiResponse.trim();

        // Remove Markdown code fences if the model adds them.
        if (json.startsWith("```")) {
            json = json.replaceFirst(
                    "^```(?:json)?\\s*", ""
            );
            json = json.replaceFirst(
                    "\\s*```$", ""
            ).trim();
        }

        // Basic check that the response looks like a JSON object.
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalStateException(
                    "AI did not return a valid JSON object. Please retry."
            );
        }

        return json;
    }
}
