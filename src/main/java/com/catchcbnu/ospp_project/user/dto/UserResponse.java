package com.catchcbnu.ospp_project.user.dto;

public record UserResponse(
        Long userId,
        String email,
        String nickname,
        String college,
        String department,
        int totalSubmissionCount,
        int exp,
        int level,
        RankingSummary ranking
) {
    public record RankingSummary(
            int overallRank,
            int collegeRank,
            int departmentRank
    ){
    }
}