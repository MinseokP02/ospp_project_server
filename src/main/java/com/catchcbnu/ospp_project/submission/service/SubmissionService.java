package com.catchcbnu.ospp_project.submission.service;

import com.catchcbnu.ospp_project.exp.service.LevelEngine;
import com.catchcbnu.ospp_project.sensor.entity.Sensor;
import com.catchcbnu.ospp_project.sensor.repository.SensorRepository;
import com.catchcbnu.ospp_project.submission.dto.SubmissionAvailabilityResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionRequest;
import com.catchcbnu.ospp_project.submission.dto.SubmissionResponse;
import com.catchcbnu.ospp_project.submission.entity.Submission;
import com.catchcbnu.ospp_project.submission.repository.SubmissionRepository;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class SubmissionService {

    private static final int DEFAULT_REWARD_EXP = 10;

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final SensorRepository sensorRepository;
    private final LevelEngine levelEngine;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            UserRepository userRepository,
            SensorRepository sensorRepository,
            LevelEngine levelEngine
    ) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.sensorRepository = sensorRepository;
        this.levelEngine = levelEngine;
    }

    @Transactional(readOnly = true)
    public SubmissionAvailabilityResponse getAvailability(Long userId, Long sensorId) {
        sensorRepository.findById(sensorId)
                .orElseThrow(() -> new IllegalArgumentException("센서를 찾을 수 없습니다."));

        LocalDateTime currentTimeSlot = currentTimeSlot();
        boolean alreadySubmitted = submissionRepository.existsByUserIdAndSensorIdAndTimeSlot(
                userId,
                sensorId,
                currentTimeSlot
        );

        return new SubmissionAvailabilityResponse(
                sensorId,
                !alreadySubmitted,
                currentTimeSlot,
                alreadySubmitted,
                alreadySubmitted ? currentTimeSlot.plusHours(1) : null
        );
    }

    @Transactional
    public SubmissionResponse submit(Long userId, SubmissionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Sensor sensor = sensorRepository.findById(request.sensorId())
                .orElseThrow(() -> new IllegalArgumentException("센서를 찾을 수 없습니다."));

        LocalDateTime timeSlot = request.measuredAt().truncatedTo(ChronoUnit.HOURS);
        boolean alreadySubmitted = submissionRepository.existsByUserIdAndSensorIdAndTimeSlot(
                userId,
                sensor.getId(),
                timeSlot
        );
        if (alreadySubmitted) {
            throw new IllegalStateException("이미 해당 시간대에 전송했습니다.");
        }

        int beforeLevel = user.getLevel();
        Submission submission = new Submission(
                user,
                sensor,
                request.temperature(),
                request.humidity(),
                request.eco2(),
                request.airQuality(),
                request.rssi(),
                request.latitude(),
                request.longitude(),
                request.measuredAt(),
                timeSlot,
                DEFAULT_REWARD_EXP
        );

        Submission saved = submissionRepository.save(submission);
        user.increaseSubmissionCount();
        levelEngine.applyExp(user, DEFAULT_REWARD_EXP);

        return new SubmissionResponse(
                saved.getId(),
                user.getId(),
                sensor.getId(),
                DEFAULT_REWARD_EXP,
                user.getExp(),
                user.getLevel(),
                user.getLevel() > beforeLevel,
                user.getTotalSubmissionCount(),
                false,
                timeSlot.plusHours(1)
        );
    }

    private LocalDateTime currentTimeSlot() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
    }
}
