package com.catchcbnu.ospp_project.submission.dto;

import com.catchcbnu.ospp_project.character.dto.SubmissionCharacterReward;

import java.time.LocalDateTime;

public record SubmissionResponse(
        Long submissionId,
        Long userId,
        Long sensorId,
        String sensorName,

        // 데이터 수집 기본 보상
        Integer rewardExp,

        // 보상 적용 후 사용자 상태
        Integer totalExp,
        Integer level,
        Boolean levelUp,
        Integer totalSubmissionCount,

        // 캐릭터는 있을 때만 수집됨
        Boolean characterCollected,
        SubmissionCharacterReward characterReward,

        LocalDateTime nextAvailableAt,
        LocalDateTime createdAt
) {
}