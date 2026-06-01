package com.catchcbnu.ospp_project.submission.dto;

import java.util.List;

public record SubmissionPageResponse(
        Integer page,
        Integer size,
        Long totalElements,
        List<SubmissionItemResponse> submissions
) {
}