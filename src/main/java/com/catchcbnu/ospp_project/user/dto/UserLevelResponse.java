package com.catchcbnu.ospp_project.user.dto;

public record UserLevelResponse(
        Long userId,
        Integer level,
        Integer currentExp,
        Integer currentLevelMinExp,
        Integer nextLevelExp,
        Integer requiredExpToNextLevel,
        Double progressRate
) {
}