package com.catchcbnu.ospp_project.submission.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubmissionRequest(
        @NotNull(message = "sensorId는 필수입니다.")
        Long sensorId,

        @NotNull(message = "temperature는 필수입니다.")
        BigDecimal temperature,

        @NotNull(message = "humidity는 필수입니다.")
        BigDecimal humidity,

        @NotNull(message = "eco2는 필수입니다.")
        Integer eco2,

        @NotNull(message = "airQuality는 필수입니다.")
        Integer airQuality,

        @NotNull(message = "rssi는 필수입니다.")
        Integer rssi,

        BigDecimal latitude,

        BigDecimal longitude,

        @NotNull(message = "measuredAt은 필수입니다.")
        LocalDateTime measuredAt
) {
}