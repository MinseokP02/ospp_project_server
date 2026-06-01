package com.catchcbnu.ospp_project.submission.repository;

import com.catchcbnu.ospp_project.submission.entity.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    boolean existsByUser_IdAndSensor_IdAndTimeSlot(
            Long userId,
            Long sensorId,
            LocalDateTime timeSlot
    );

    Optional<Submission> findTopBySensorIdOrderBySubmittedAtDesc(Long sensorId);

    Page<Submission> findByUser_IdOrderBySubmittedAtDesc(
            Long userId,
            Pageable pageable
    );
}