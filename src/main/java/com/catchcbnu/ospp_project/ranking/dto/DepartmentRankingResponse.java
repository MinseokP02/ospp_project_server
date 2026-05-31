package com.catchcbnu.ospp_project.ranking.dto;

public record DepartmentRankingResponse(
        Integer rank,
        String college,
        String department,
        Long totalSubmissionCount,
        Long userCount
) {
}