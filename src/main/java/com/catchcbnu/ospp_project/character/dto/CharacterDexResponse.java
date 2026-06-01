package com.catchcbnu.ospp_project.character.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CharacterDexResponse(
        Integer totalCount,
        Integer collectedCount,
        List<CharacterDexItem> characters
) {
    public record CharacterDexItem(
            Long characterId,
            String characterName,
            String rarity,
            String description,
            Double baseSpawnRate,
            Boolean collected,
            Integer collectedCount,
            LocalDateTime firstFoundAt
    ) {
    }
}