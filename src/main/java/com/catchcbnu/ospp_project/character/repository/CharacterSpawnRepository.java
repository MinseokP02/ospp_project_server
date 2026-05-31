package com.catchcbnu.ospp_project.character.repository;

import com.catchcbnu.ospp_project.character.domain.CharacterSpawn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CharacterSpawnRepository extends JpaRepository<CharacterSpawn, Long> {

    List<CharacterSpawn> findByActiveTrueOrderBySpawnedAtDesc();

    Optional<CharacterSpawn> findFirstBySensorIdAndActiveTrueAndExpiresAtAfterOrderBySpawnedAtDesc(
            Long sensorId,
            LocalDateTime now
    );

    boolean existsBySensorIdAndActiveTrueAndExpiresAtAfter(
            Long sensorId,
            LocalDateTime now
    );
}