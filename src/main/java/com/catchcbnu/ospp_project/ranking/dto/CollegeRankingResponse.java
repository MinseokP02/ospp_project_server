package com.catchcbnu.ospp_project.ranking.dto;

public record CollegeRankingResponse(
        Integer rank,
        String college,
        Long totalSubmissionCount,
        Long userCount
) {
}