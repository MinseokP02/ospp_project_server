package com.catchcbnu.ospp_project.sensor.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LatestSensorDataResponse(
        BigDecimal temperature,
        BigDecimal humidity,
        Integer eco2,
        Integer airQuality,
        Integer rssi,
        LocalDateTime measuredAt,
        LocalDateTime receivedAt
) {
}
