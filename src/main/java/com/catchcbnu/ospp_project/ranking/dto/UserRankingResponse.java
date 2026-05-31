package com.catchcbnu.ospp_project.ranking.dto;

import java.time.LocalDateTime;

public record UserRankingResponse(
        Integer rank,
        Long userId,
        String nickname,
        String college,
        String department,
        Integer totalSubmissionCount,
        Integer exp,
        Integer level,
        LocalDateTime lastActiveAt
) {
}