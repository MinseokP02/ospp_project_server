package com.catchcbnu.ospp_project.character.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CharacterSpawnListResponse(
        List<SpawnItem> spawns
) {
    public record SpawnItem(
            Long spawnId,
            Long sensorId,
            String sensorName,
            Long characterId,
            String characterName,
            String rarity,
            LocalDateTime spawnedAt,
            LocalDateTime expiresAt
    ) {
    }
}