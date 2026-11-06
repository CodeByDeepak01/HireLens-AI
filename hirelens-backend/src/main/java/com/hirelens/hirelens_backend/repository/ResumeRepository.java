
package com.hirelens.hirelens_backend.repository;

import com.hirelens.hirelens_backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
