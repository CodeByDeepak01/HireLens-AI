
package com.hirelens.hirelens_backend.controller;

import com.hirelens.hirelens_backend.entity.Resume;
import com.hirelens.hirelens_backend.service.ResumeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> uploadResume(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        Resume resume = resumeService.uploadResume(file);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "id", resume.getId(),
                        "fileName", resume.getFileName(),
                        "message", "Resume uploaded and text extracted successfully",
                        "extractedText", resume.getExtractedText()
                )
        );
    }
}
