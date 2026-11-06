
package com.hirelens.hirelens_backend.repository;

import com.hirelens.hirelens_backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
