// Sensor/Scheduler 완성 전 테스트용

package com.catchcbnu.ospp_project.character.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CharacterSpawnCreateRequest(
        @NotNull(message = "sensorId는 필수입니다.")
        Long sensorId,

        @NotBlank(message = "sensorName은 필수입니다.")
        String sensorName,

        Integer durationMinutes
) {
}