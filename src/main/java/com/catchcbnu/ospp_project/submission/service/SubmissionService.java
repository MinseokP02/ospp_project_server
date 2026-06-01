package com.catchcbnu.ospp_project.submission.service;

import com.catchcbnu.ospp_project.activity.entity.ActivityType;
import com.catchcbnu.ospp_project.activity.service.UserActivityService;
import com.catchcbnu.ospp_project.character.dto.SubmissionCharacterReward;
import com.catchcbnu.ospp_project.character.service.CharacterService;
import com.catchcbnu.ospp_project.exp.service.LevelEngine;
import com.catchcbnu.ospp_project.sensor.entity.Sensor;
import com.catchcbnu.ospp_project.sensor.repository.SensorRepository;
import com.catchcbnu.ospp_project.submission.dto.SubmissionAvailabilityResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionItemResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionPageResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionRequest;
import com.catchcbnu.ospp_project.submission.dto.SubmissionResponse;
import com.catchcbnu.ospp_project.submission.entity.Submission;
import com.catchcbnu.ospp_project.submission.repository.SubmissionRepository;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class SubmissionService {

    private static final int DEFAULT_REWARD_EXP = 10;

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final SensorRepository sensorRepository;
    private final LevelEngine levelEngine;
    private final UserActivityService userActivityService;
    private final CharacterService characterService;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            UserRepository userRepository,
            SensorRepository sensorRepository,
            LevelEngine levelEngine,
            UserActivityService userActivityService,
            CharacterService characterService
    ) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.sensorRepository = sensorRepository;
        this.levelEngine = levelEngine;
        this.userActivityService = userActivityService;
        this.characterService = characterService;
    }

    @Transactional(readOnly = true)
    public SubmissionAvailabilityResponse getAvailability(Long userId, Long sensorId) {
        sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "센서를 찾을 수 없습니다."
                ));

        LocalDateTime currentTimeSlot = currentTimeSlot();

        boolean alreadySubmitted =
                submissionRepository.existsByUser_IdAndSensor_IdAndTimeSlot(
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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "사용자를 찾을 수 없습니다."
                ));

        Sensor sensor = sensorRepository.findById(request.sensorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "센서를 찾을 수 없습니다."
                ));

        if (!Boolean.TRUE.equals(sensor.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "비활성화된 센서입니다."
            );
        }

        /*
         * 중복 방지 기준은 서버 현재 시간 기준.
         * measuredAt은 센서가 실제로 측정한 시간으로만 저장한다.
         */
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeSlot = now.truncatedTo(ChronoUnit.HOURS);

        boolean alreadySubmitted =
                submissionRepository.existsByUser_IdAndSensor_IdAndTimeSlot(
                        user.getId(),
                        sensor.getId(),
                        timeSlot
                );

        if (alreadySubmitted) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 해당 시간대에 이 센서 데이터를 전송했습니다."
            );
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
                now,
                timeSlot,
                DEFAULT_REWARD_EXP
        );

        Submission savedSubmission = submissionRepository.save(submission);

        /*
         * 데이터 수집 자체는 항상 보상.
         * 캐릭터 여부와 무관하게 실행된다.
         */
        user.increaseSubmissionCount();

        boolean levelUpBySubmission =
                levelEngine.applyExp(user, DEFAULT_REWARD_EXP);

        userActivityService.createActivity(
                user,
                ActivityType.SUBMISSION,
                "센서 데이터 전송",
                sensor.getSensorName() + " 센서 데이터를 전송했습니다.",
                DEFAULT_REWARD_EXP
        );

        if (levelUpBySubmission) {
            userActivityService.createActivity(
                    user,
                    ActivityType.LEVEL_UP,
                    "레벨업",
                    user.getLevel() + "레벨이 되었습니다.",
                    0
            );
        }

        /*
         * 캐릭터는 조건부 보상.
         * 해당 sensorId에 출몰 중인 캐릭터가 있을 때만 수집된다.
         * 없으면 null 반환.
         */
        SubmissionCharacterReward characterReward =
                characterService.collectSpawnedCharacterIfExists(
                        user,
                        sensor.getId()
                );

        boolean characterCollected = characterReward != null;
        boolean levelUp = user.getLevel() > beforeLevel;

        return new SubmissionResponse(
                savedSubmission.getId(),
                user.getId(),
                sensor.getId(),
                sensor.getSensorName(),
                DEFAULT_REWARD_EXP,
                user.getExp(),
                user.getLevel(),
                levelUp,
                user.getTotalSubmissionCount(),
                characterCollected,
                characterReward,
                timeSlot.plusHours(1),
                savedSubmission.getSubmittedAt()
        );
    }

    @Transactional(readOnly = true)
    public SubmissionPageResponse getMySubmissions(
            Long userId,
            int page,
            int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        Pageable pageable = PageRequest.of(safePage, safeSize);

        Page<Submission> submissionPage =
                submissionRepository.findByUser_IdOrderBySubmittedAtDesc(
                        userId,
                        pageable
                );

        return new SubmissionPageResponse(
                submissionPage.getNumber(),
                submissionPage.getSize(),
                submissionPage.getTotalElements(),
                submissionPage.getContent()
                        .stream()
                        .map(submission -> new SubmissionItemResponse(
                                submission.getId(),
                                submission.getSensor().getId(),
                                submission.getSensor().getSensorName(),
                                submission.getTemperature(),
                                submission.getHumidity(),
                                submission.getEco2(),
                                submission.getAirQuality(),
                                submission.getRssi(),
                                submission.getMeasuredAt(),
                                submission.getSubmittedAt()
                        ))
                        .toList()
        );
    }

    private LocalDateTime currentTimeSlot() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
    }
}