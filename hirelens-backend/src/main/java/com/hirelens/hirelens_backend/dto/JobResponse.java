
package com.hirelens.hirelens_backend.dto;

import java.time.LocalDateTime;

public class JobResponse {

    private Long id;
    private String company;
    private String role;
    private String jobDescription;
    private LocalDateTime createdAt;

    public JobResponse() {
    }

    public JobResponse(
            Long id,
            String company,
            String role,
            String jobDescription,
            LocalDateTime createdAt) {

        this.id = id;
        this.company = company;
        this.role = role;
        this.jobDescription = jobDescription;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getRole() {
        return role;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
