package com.catchcbnu.ospp_project.character.controller;

//import com.catchcbnu.ospp_project.character.dto.CharacterCollectRequest;
import com.catchcbnu.ospp_project.character.dto.CharacterListResponse;
import com.catchcbnu.ospp_project.character.dto.CharacterSpawnCreateRequest;
import com.catchcbnu.ospp_project.character.dto.CharacterSpawnListResponse;
import com.catchcbnu.ospp_project.character.dto.MyCharacterListResponse;
import com.catchcbnu.ospp_project.character.service.CharacterService;
import com.catchcbnu.ospp_project.common.response.ApiResponse;
import com.catchcbnu.ospp_project.character.dto.CharacterDexResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/api/characters")
    public ResponseEntity<ApiResponse<CharacterListResponse>> getCharacters() {
        CharacterListResponse response = characterService.getCharacters();

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "캐릭터 목록 조회 성공", response)
        );
    }

    @GetMapping("/api/characters/spawns")
    public ResponseEntity<ApiResponse<CharacterSpawnListResponse>> getCurrentSpawns() {
        CharacterSpawnListResponse response = characterService.getCurrentSpawns();

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "현재 출몰 캐릭터 조회 성공", response)
        );
    }

    @GetMapping("/api/users/me/characters")
    public ResponseEntity<ApiResponse<MyCharacterListResponse>> getMyCharacters(
            @AuthenticationPrincipal Long userId
    ) {
        MyCharacterListResponse response = characterService.getMyCharacters(userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "내 캐릭터 수집 기록 조회 성공", response)
        );
    }

    @GetMapping("/api/users/me/characters/dex")
    public ResponseEntity<ApiResponse<CharacterDexResponse>> getMyCharacterDex(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterDexResponse response = characterService.getMyCharacterDex(userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "내 캐릭터 도감 조회 성공", response)
        );
    }

    @PostMapping("/characters/spawns/test")
    public ResponseEntity<ApiResponse<CharacterSpawnListResponse.SpawnItem>> createRandomSpawnForTest(
            @Valid @RequestBody CharacterSpawnCreateRequest request
    ) {
        CharacterSpawnListResponse.SpawnItem response =
                characterService.createRandomSpawnForTest(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "테스트용 캐릭터 출몰 생성 성공", response));
    }



    // 테스트용. 나중에 Submission API에서 캐릭터 발견 처리를 연결하면 제거 가능.
//    @PostMapping("/api/users/me/characters")
//    public ResponseEntity<ApiResponse<MyCharacterListResponse.MyCharacterItem>> collectCharacterForTest(
//            @AuthenticationPrincipal Long userId,
//            @Valid @RequestBody CharacterCollectRequest request
//    ) {
//        MyCharacterListResponse.MyCharacterItem response =
//                characterService.collectCharacter(userId, request);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.success(HttpStatus.CREATED, "캐릭터 수집 성공", response));
//    }
}