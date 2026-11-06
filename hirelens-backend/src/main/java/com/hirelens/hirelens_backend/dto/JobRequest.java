
package com.hirelens.hirelens_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JobRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name cannot exceed 150 characters")
    private String company;

    @NotBlank(message = "Job role is required")
    @Size(max = 150, message = "Job role cannot exceed 150 characters")
    private String role;

    @NotBlank(message = "Job description is required")
    @Size(max = 30000, message = "Job description is too long")
    private String jobDescription;

    public JobRequest() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
