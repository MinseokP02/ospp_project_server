package com.catchcbnu.ospp_project.submission.dto;

import java.time.LocalDateTime;

public record SubmissionResponse(
        Long submissionId,
        Long userId,
        Long sensorId,
        int rewardExp,
        int totalExp,
        int level,
        boolean levelUp,
        int totalSubmissionCount,
        boolean characterSpawned,
        LocalDateTime nextAvailableAt
) {
}
