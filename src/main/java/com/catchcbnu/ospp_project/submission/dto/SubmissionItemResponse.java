package com.catchcbnu.ospp_project.submission.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubmissionItemResponse(
        Long submissionId,
        Long sensorId,
        String sensorName,
        BigDecimal temperature,
        BigDecimal humidity,
        Integer eco2,
        Integer airQuality,
        Integer rssi,
        LocalDateTime measuredAt,
        LocalDateTime submittedAt
) {
}