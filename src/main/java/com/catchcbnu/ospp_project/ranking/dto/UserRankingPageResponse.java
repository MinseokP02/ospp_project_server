package com.catchcbnu.ospp_project.ranking.dto;

import java.util.List;

public record UserRankingPageResponse(
        Integer page,
        Integer size,
        Long totalElements,
        List<UserRankingResponse> rankings
) {
}