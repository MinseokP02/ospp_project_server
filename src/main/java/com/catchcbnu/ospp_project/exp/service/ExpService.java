package com.catchcbnu.ospp_project.exp.service;

import com.catchcbnu.ospp_project.activity.entity.ActivityType;
import com.catchcbnu.ospp_project.activity.service.UserActivityService;
import com.catchcbnu.ospp_project.exp.dto.ExpEventRequest;
import com.catchcbnu.ospp_project.exp.dto.ExpEventResponse;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpService {

    private final UserRepository userRepository;
    private final LevelEngine levelEngine;
    private final UserActivityService userActivityService;

    public ExpService(
            UserRepository userRepository,
            LevelEngine levelEngine,
            UserActivityService userActivityService
    ) {
        this.userRepository = userRepository;
        this.levelEngine = levelEngine;
        this.userActivityService = userActivityService;
    }

    @Transactional
    public ExpEventResponse addExp(ExpEventRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (request.amount() <= 0) {
            throw new IllegalArgumentException("경험치는 1 이상이어야 합니다.");
        }

        boolean levelUp = levelEngine.applyExp(user, request.amount());

        userActivityService.createActivity(
                user,
                ActivityType.EXP,
                "경험치 획득",
                request.eventType() + " 활동으로 경험치를 획득했습니다.",
                request.amount()
        );

        if (levelUp) {
            userActivityService.createActivity(
                    user,
                    ActivityType.LEVEL_UP,
                    "레벨업",
                    user.getLevel() + "레벨이 되었습니다.",
                    0
            );
        }

        return new ExpEventResponse(
                user.getId(),
                user.getLevel(),
                user.getExp(),
                "경험치가 지급되었습니다."
        );
    }
}