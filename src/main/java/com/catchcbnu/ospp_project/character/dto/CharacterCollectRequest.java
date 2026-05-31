package com.catchcbnu.ospp_project.character.dto;

import jakarta.validation.constraints.NotNull;

public record CharacterCollectRequest(
        @NotNull(message = "characterId는 필수입니다.")
        Long characterId,

        Long sensorId,

        String sensorName
) {
}