
package com.hirelens.hirelens_backend.controller;

import com.hirelens.hirelens_backend.dto.JobRequest;
import com.hirelens.hirelens_backend.dto.JobResponse;
import com.hirelens.hirelens_backend.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobRequest request) {

        JobResponse response = jobService.createJob(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
