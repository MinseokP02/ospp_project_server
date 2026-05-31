package com.catchcbnu.ospp_project.ranking.dto;

public record MyRankingResponse(
        Integer overallRank,
        Integer collegeRank,
        Integer departmentRank,
        Integer totalSubmissionCount,
        Integer exp,
        Integer level
) {
}