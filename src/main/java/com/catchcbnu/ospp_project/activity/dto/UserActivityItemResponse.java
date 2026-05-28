package com.catchcbnu.ospp_project.activity.dto;

import java.time.LocalDateTime;

public record UserActivityItemResponse(
        Long activityId,
        String type,
        String title,
        String description,
        Integer expChange,
        LocalDateTime createdAt
) {
}