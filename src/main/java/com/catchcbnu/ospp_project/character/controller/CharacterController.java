package com.catchcbnu.ospp_project.character.controller;

import com.catchcbnu.ospp_project.character.dto.CharacterCatchRequest;
import com.catchcbnu.ospp_project.character.dto.CharacterResponse;
import com.catchcbnu.ospp_project.character.service.CharacterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {
    @GetMapping("/test")
    public String test() {
        return "character controller connected";
    }

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/spawns")
    public CharacterResponse catchCharacter(@RequestBody CharacterCatchRequest request) {
        return characterService.catchCharacter(request);
    }

    @GetMapping("/users/{userId}")
    public List<CharacterResponse> getUserCharacters(@PathVariable Long userId) {
        return characterService.getUserCharacters(userId);
    }
}