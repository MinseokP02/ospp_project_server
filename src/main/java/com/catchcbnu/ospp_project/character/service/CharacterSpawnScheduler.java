package com.catchcbnu.ospp_project.character.service;

import com.catchcbnu.ospp_project.sensor.entity.Sensor;
import com.catchcbnu.ospp_project.sensor.repository.SensorRepository;
import com.catchcbnu.ospp_project.character.service.CharacterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;

@Component
public class CharacterSpawnScheduler {

    private static final int SPAWN_DURATION_MINUTES = 60;

    private final SensorRepository sensorRepository;
    private final CharacterService characterService;

    public CharacterSpawnScheduler(
            SensorRepository sensorRepository,
            CharacterService characterService
    ) {
        this.sensorRepository = sensorRepository;
        this.characterService = characterService;
    }

    //@Scheduled(cron = "0 0 * * * *")
    @Scheduled(fixedRate = 30000)
    public void spawnRandomCharacterEveryHour() {
        List<Sensor> activeSensors = sensorRepository.findByActiveTrueOrderByIdAsc();

        if (activeSensors.isEmpty()) {
            return;
        }

        Collections.shuffle(activeSensors);

        for (Sensor sensor : activeSensors) {
            boolean created = characterService.createRandomSpawnIfAbsent(
                    sensor.getId(),
                    sensor.getSensorName(),
                    SPAWN_DURATION_MINUTES
            );

            if (created) {
                return;
            }
        }
    }
}