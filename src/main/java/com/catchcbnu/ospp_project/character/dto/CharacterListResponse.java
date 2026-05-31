package com.catchcbnu.ospp_project.character.dto;

import java.util.List;

public record CharacterListResponse(
        List<CharacterItem> characters
) {
    public record CharacterItem(
            Long characterId,
            String name,
            String rarity,
            String description,
            Double baseSpawnRate
    ) {
    }
}