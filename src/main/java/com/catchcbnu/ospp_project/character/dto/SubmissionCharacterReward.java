// SubmissionService에서 응답에 넣을 보상 DTO

package com.catchcbnu.ospp_project.character.dto;

public record SubmissionCharacterReward(
        Long characterId,
        String characterName,
        String rarity,
        Integer bonusExp
) {
}