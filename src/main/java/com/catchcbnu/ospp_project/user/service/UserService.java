package com.catchcbnu.ospp_project.user.service;

import com.catchcbnu.ospp_project.exp.service.LevelEngine;
import com.catchcbnu.ospp_project.user.dto.UserLevelResponse;
import com.catchcbnu.ospp_project.user.dto.UserResponse;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LevelEngine levelEngine;

    public UserService(
            UserRepository userRepository,
            LevelEngine levelEngine
    ) {
        this.userRepository = userRepository;
        this.levelEngine = levelEngine;
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = findUserById(userId);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserLevelResponse getMyLevel(Long userId) {
        User user = findUserById(userId);

        int level = user.getLevel();
        int totalExp = user.getExp();

        int currentLevelMinExp = levelEngine.minExpForLevel(level);
        int nextLevelExp = levelEngine.nextLevelExp(level);
        int requiredExpToNextLevel = levelEngine.requiredExpToNextLevel(level, totalExp);
        double progressRate = levelEngine.progressRate(level, totalExp);

        return new UserLevelResponse(
                user.getId(),
                level,
                totalExp,
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
        UserResponse.RankingSummary ranking = calculateRanking(user);

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getCollege(),
                user.getDepartment(),
                user.getTotalSubmissionCount(),
                user.getExp(),
                user.getLevel(),
                ranking
        );
    }

    private UserResponse.RankingSummary calculateRanking(User user) {
        List<User> overallUsers =
                userRepository.findAllByOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc();

        List<User> collegeUsers =
                userRepository.findByCollegeOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(
                        user.getCollege()
                );

        List<User> departmentUsers =
                userRepository.findByCollegeAndDepartmentOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(
                        user.getCollege(),
                        user.getDepartment()
                );

        return new UserResponse.RankingSummary(
                findRank(user.getId(), overallUsers),
                findRank(user.getId(), collegeUsers),
                findRank(user.getId(), departmentUsers)
        );
    }

    private int findRank(Long userId, List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                return i + 1;
            }
        }

        return 0;
    }
}