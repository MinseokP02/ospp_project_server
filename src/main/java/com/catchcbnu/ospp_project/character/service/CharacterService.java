package com.catchcbnu.ospp_project.character.service;

import com.catchcbnu.ospp_project.activity.entity.ActivityType;
import com.catchcbnu.ospp_project.activity.service.UserActivityService;
import com.catchcbnu.ospp_project.character.domain.CharacterInfo;
import com.catchcbnu.ospp_project.character.domain.CharacterSpawn;
import com.catchcbnu.ospp_project.character.domain.UserCharacter;
import com.catchcbnu.ospp_project.character.dto.CharacterListResponse;
import com.catchcbnu.ospp_project.character.dto.CharacterSpawnCreateRequest;
import com.catchcbnu.ospp_project.character.dto.CharacterSpawnListResponse;
import com.catchcbnu.ospp_project.character.dto.MyCharacterListResponse;
import com.catchcbnu.ospp_project.character.dto.SubmissionCharacterReward;
import com.catchcbnu.ospp_project.character.repository.CharacterRepository;
import com.catchcbnu.ospp_project.character.repository.CharacterSpawnRepository;
import com.catchcbnu.ospp_project.character.repository.UserCharacterRepository;
import com.catchcbnu.ospp_project.exp.service.LevelEngine;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterSpawnRepository characterSpawnRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final UserRepository userRepository;
    private final LevelEngine levelEngine;
    private final UserActivityService userActivityService;
    private final Random random = new Random();

    public CharacterService(
            CharacterRepository characterRepository,
            CharacterSpawnRepository characterSpawnRepository,
            UserCharacterRepository userCharacterRepository,
            UserRepository userRepository,
            LevelEngine levelEngine,
            UserActivityService userActivityService
    ) {
        this.characterRepository = characterRepository;
        this.characterSpawnRepository = characterSpawnRepository;
        this.userCharacterRepository = userCharacterRepository;
        this.userRepository = userRepository;
        this.levelEngine = levelEngine;
        this.userActivityService = userActivityService;
    }

    @Transactional(readOnly = true)
    public CharacterListResponse getCharacters() {
        List<CharacterListResponse.CharacterItem> characters =
                characterRepository.findAllByOrderByIdAsc()
                        .stream()
                        .map(character -> new CharacterListResponse.CharacterItem(
                                character.getId(),
                                character.getName(),
                                character.getRarity(),
                                character.getDescription(),
                                character.getBaseSpawnRate()
                        ))
                        .toList();

        return new CharacterListResponse(characters);
    }

    @Transactional(readOnly = true)
    public CharacterSpawnListResponse getCurrentSpawns() {
        LocalDateTime now = LocalDateTime.now();

        List<CharacterSpawnListResponse.SpawnItem> spawns =
                characterSpawnRepository.findByActiveTrueOrderBySpawnedAtDesc()
                        .stream()
                        .filter(spawn -> spawn.isAvailableAt(now))
                        .map(spawn -> new CharacterSpawnListResponse.SpawnItem(
                                spawn.getId(),
                                spawn.getSensorId(),
                                spawn.getSensorName(),
                                spawn.getCharacter().getId(),
                                spawn.getCharacter().getName(),
                                spawn.getCharacter().getRarity(),
                                spawn.getSpawnedAt(),
                                spawn.getExpiresAt()
                        ))
                        .toList();

        return new CharacterSpawnListResponse(spawns);
    }

    @Transactional(readOnly = true)
    public MyCharacterListResponse getMyCharacters(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        List<MyCharacterListResponse.MyCharacterItem> characters =
                userCharacterRepository.findByUser_IdOrderByFoundAtDesc(userId)
                        .stream()
                        .map(userCharacter -> new MyCharacterListResponse.MyCharacterItem(
                                userCharacter.getId(),
                                userCharacter.getCharacter().getId(),
                                userCharacter.getCharacter().getName(),
                                userCharacter.getCharacter().getRarity(),
                                userCharacter.getSensorId(),
                                userCharacter.getSensorName(),
                                userCharacter.getFoundAt()
                        ))
                        .toList();

        return new MyCharacterListResponse(characters);
    }

    /**
     * Sensor/Scheduler 구현 전 테스트용.
     * 특정 센서에 랜덤 캐릭터를 출몰시킨다.
     */
    @Transactional
    public CharacterSpawnListResponse.SpawnItem createRandomSpawnForTest(
            CharacterSpawnCreateRequest request
    ) {
        int durationMinutes = request.durationMinutes() == null
                ? 60
                : Math.max(request.durationMinutes(), 1);

        CharacterInfo selectedCharacter = selectRandomCharacterBySpawnRate();

        LocalDateTime now = LocalDateTime.now();

        CharacterSpawn spawn = new CharacterSpawn(
                selectedCharacter,
                request.sensorId(),
                request.sensorName(),
                now,
                now.plusMinutes(durationMinutes)
        );

        CharacterSpawn saved = characterSpawnRepository.save(spawn);

        return new CharacterSpawnListResponse.SpawnItem(
                saved.getId(),
                saved.getSensorId(),
                saved.getSensorName(),
                saved.getCharacter().getId(),
                saved.getCharacter().getName(),
                saved.getCharacter().getRarity(),
                saved.getSpawnedAt(),
                saved.getExpiresAt()
        );
    }

    /**
     * 나중에 Scheduler에서 사용할 메서드.
     * 특정 센서에 이미 출몰 중인 캐릭터가 없으면 랜덤 캐릭터를 생성한다.
     *
     *
     */
    @Transactional
    public boolean createRandomSpawnIfAbsent(
            Long sensorId,
            String sensorName,
            int durationMinutes
    ) {
        LocalDateTime now = LocalDateTime.now();

        boolean alreadyExists = characterSpawnRepository
                .existsBySensorIdAndActiveTrueAndExpiresAtAfter(sensorId, now);

        if (alreadyExists) {
            return false;
        }

        CharacterInfo selectedCharacter = selectRandomCharacterBySpawnRate();

        CharacterSpawn spawn = new CharacterSpawn(
                selectedCharacter,
                sensorId,
                sensorName,
                now,
                now.plusMinutes(durationMinutes)
        );

        characterSpawnRepository.save(spawn);

        return true;
    }

    /**
     * 핵심 메서드.
     * SubmissionService에서 센서 데이터 전송 성공 후 호출해야 한다.
     *
     * 사용자가 데이터를 전송한 sensorId에 출몰 중인 캐릭터가 있으면 자동 수집한다.
     */
    @Transactional
    public SubmissionCharacterReward collectSpawnedCharacterIfExists(
            User user,
            Long sensorId
    ) {
        LocalDateTime now = LocalDateTime.now();

        CharacterSpawn spawn = characterSpawnRepository
                .findFirstBySensorIdAndActiveTrueAndExpiresAtAfterOrderBySpawnedAtDesc(sensorId, now)
                .orElse(null);

        if (spawn == null) {
            return null;
        }

        boolean alreadyCollected = userCharacterRepository.existsByUser_IdAndSpawn_Id(
                user.getId(),
                spawn.getId()
        );

        if (alreadyCollected) {
            return null;
        }

        CharacterInfo character = spawn.getCharacter();

        UserCharacter userCharacter = new UserCharacter(
                user,
                character,
                spawn,
                spawn.getSensorId(),
                spawn.getSensorName()
        );

        userCharacterRepository.save(userCharacter);

        int bonusExp = calculateCharacterBonusExp(character.getRarity());

        boolean levelUp = levelEngine.applyExp(user, bonusExp);

        userActivityService.createActivity(
                user,
                ActivityType.CHARACTER,
                "캐릭터 발견",
                character.getName() + " 캐릭터를 발견했습니다.",
                bonusExp
        );

        if (levelUp) {
            userActivityService.createActivity(
                    user,
                    ActivityType.LEVEL_UP,
                    "레벨업",
                    user.getLevel() + "레벨이 되었습니다.",
                    0
            );
        }

        return new SubmissionCharacterReward(
                character.getId(),
                character.getName(),
                character.getRarity(),
                bonusExp
        );
    }

    private CharacterInfo selectRandomCharacterBySpawnRate() {
        List<CharacterInfo> characters = characterRepository.findAllByOrderByIdAsc();

        if (characters.isEmpty()) {
            throw new IllegalStateException("등록된 캐릭터가 없습니다.");
        }

        double totalWeight = characters.stream()
                .map(CharacterInfo::getBaseSpawnRate)
                .filter(rate -> rate != null && rate > 0)
                .mapToDouble(Double::doubleValue)
                .sum();

        if (totalWeight <= 0) {
            return characters.stream()
                    .min(Comparator.comparingLong(CharacterInfo::getId))
                    .orElseThrow();
        }

        double randomValue = random.nextDouble() * totalWeight;
        double cumulative = 0.0;

        for (CharacterInfo character : characters) {
            double weight = character.getBaseSpawnRate() == null
                    ? 0.0
                    : Math.max(character.getBaseSpawnRate(), 0.0);

            cumulative += weight;

            if (randomValue <= cumulative) {
                return character;
            }
        }

        return characters.get(characters.size() - 1);
    }

    private int calculateCharacterBonusExp(String rarity) {
        if (rarity == null) {
            return 20;
        }

        return switch (rarity.toUpperCase()) {
            case "LEGENDARY" -> 100;
            case "RARE" -> 50;
            case "COMMON" -> 20;
            default -> 20;
        };
    }
}