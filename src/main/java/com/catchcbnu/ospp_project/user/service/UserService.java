package com.catchcbnu.ospp_project.user.service;

import com.catchcbnu.ospp_project.exp.service.LevelEngine;
import com.catchcbnu.ospp_project.user.dto.UserLevelResponse;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.dto.UserResponse;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LevelEngine levelEngine;

    public UserService(UserRepository userRepository, LevelEngine levelEngine) {
        this.userRepository = userRepository;
        this.levelEngine = levelEngine;
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserLevelResponse getMyLevel(Long userId) {
        User user = findUserById(userId);

        int currentExp = user.getExp();
        int level = user.getLevel();

        int currentLevelMinExp = 0;
        int nextLevelExp = levelEngine.requiredExpForNextLevel(level);
        int requiredExpToNextLevel = Math.max(nextLevelExp - currentExp, 0);

        double progressRate = 0.0;
        if (nextLevelExp > 0) {
            progressRate = ((double) currentExp / nextLevelExp) * 100.0;
        }

        return new UserLevelResponse(
                user.getId(),
                level,
                currentExp,
                currentLevelMinExp,
                nextLevelExp,
                requiredExpToNextLevel,
                progressRate
        );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getCollege(),
                user.getDepartment(),
                user.getLevel(),
                user.getExp()
        );
    }
}
