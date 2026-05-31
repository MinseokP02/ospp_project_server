package com.catchcbnu.ospp_project.character.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MyCharacterListResponse(
        List<MyCharacterItem> characters
) {
    public record MyCharacterItem(
            Long collectionId,
            Long characterId,
            String characterName,
            String rarity,
            Long sensorId,
            String sensorName,
            LocalDateTime foundAt
    ) {
    }
}