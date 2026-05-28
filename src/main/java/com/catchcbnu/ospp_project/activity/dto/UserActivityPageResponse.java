package com.catchcbnu.ospp_project.activity.dto;

import java.util.List;

public record UserActivityPageResponse(
        Integer page,
        Integer size,
        Long totalElements,
        List<UserActivityItemResponse> activities
) {
}