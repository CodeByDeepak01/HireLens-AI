
package com.hirelens.hirelens_backend.service;

import com.hirelens.hirelens_backend.dto.JobRequest;
import com.hirelens.hirelens_backend.dto.JobResponse;
import com.hirelens.hirelens_backend.entity.Job;
import com.hirelens.hirelens_backend.repository.JobRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional
    public JobResponse createJob(JobRequest request) {

        Job job = new Job();

        job.setCompany(request.getCompany().trim());
        job.setRole(request.getRole().trim());
        job.setJobDescription(
                request.getJobDescription().trim()
        );

        Job savedJob = jobRepository.save(job);

        return new JobResponse(
                savedJob.getId(),
                savedJob.getCompany(),
                savedJob.getRole(),
                savedJob.getJobDescription(),
                savedJob.getCreatedAt()
        );
    }
}
