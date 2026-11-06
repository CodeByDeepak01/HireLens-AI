
package com.hirelens.hirelens_backend.repository;

import com.hirelens.hirelens_backend.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findByResumeId(Long resumeId);

    List<Analysis> findByJobId(Long jobId);
}
