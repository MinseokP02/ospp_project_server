package com.catchcbnu.ospp_project.sensor.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SensorResponse(
        Long sensorId,
        String sensorName,
        String locationName,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean isActive,
        LocalDateTime lastUpdatedAt,
        LatestSensorDataResponse latestData
) {
}
