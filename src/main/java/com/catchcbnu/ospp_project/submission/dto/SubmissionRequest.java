package com.catchcbnu.ospp_project.submission.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubmissionRequest(
        @NotNull(message = "센서 ID는 필수입니다.")
        Long sensorId,

        BigDecimal temperature,

        BigDecimal humidity,

        Integer eco2,

        Integer airQuality,

        Integer rssi,

        BigDecimal latitude,

        BigDecimal longitude,

        @NotNull(message = "측정 시간은 필수입니다.")
        LocalDateTime measuredAt
) {
}
