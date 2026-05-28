package com.catchcbnu.ospp_project.activity.service;

import com.catchcbnu.ospp_project.activity.dto.UserActivityItemResponse;
import com.catchcbnu.ospp_project.activity.dto.UserActivityPageResponse;
import com.catchcbnu.ospp_project.activity.entity.ActivityType;
import com.catchcbnu.ospp_project.activity.entity.UserActivity;
import com.catchcbnu.ospp_project.activity.repository.UserActivityRepository;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;

    public UserActivityService(
            UserActivityRepository userActivityRepository,
            UserRepository userRepository
    ) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserActivityPageResponse getMyActivities(
            Long userId,
            int page,
            int size
    ) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        Pageable pageable = PageRequest.of(safePage, safeSize);

        Page<UserActivity> activityPage =
                userActivityRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);

        return new UserActivityPageResponse(
                activityPage.getNumber(),
                activityPage.getSize(),
                activityPage.getTotalElements(),
                activityPage.getContent()
                        .stream()
                        .map(this::toItemResponse)
                        .toList()
        );
    }

    @Transactional
    public void createActivity(
            User user,
            ActivityType type,
            String title,
            String description,
            int expChange
    ) {
        UserActivity activity = new UserActivity(
                user,
                type,
                title,
                description,
                expChange
        );

        userActivityRepository.save(activity);
    }

    private UserActivityItemResponse toItemResponse(UserActivity activity) {
        return new UserActivityItemResponse(
                activity.getId(),
                activity.getType().name(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getExpChange(),
                activity.getCreatedAt()
        );
    }
}