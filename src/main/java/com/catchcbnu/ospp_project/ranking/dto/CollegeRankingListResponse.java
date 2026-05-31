package com.catchcbnu.ospp_project.ranking.dto;

import java.util.List;

public record CollegeRankingListResponse(
        List<CollegeRankingResponse> rankings
) {
}