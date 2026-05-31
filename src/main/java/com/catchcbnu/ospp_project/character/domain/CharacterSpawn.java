package com.catchcbnu.ospp_project.character.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_spawns")
public class CharacterSpawn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id")
    private CharacterInfo character;

    @Column(name = "sensor_id", nullable = false)
    private Long sensorId;

    @Column(name = "sensor_name", nullable = false, length = 100)
    private String sensorName;

    @Column(name = "spawned_at", nullable = false)
    private LocalDateTime spawnedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    protected CharacterSpawn() {
    }

    public CharacterSpawn(
            CharacterInfo character,
            Long sensorId,
            String sensorName,
            LocalDateTime spawnedAt,
            LocalDateTime expiresAt
    ) {
        this.character = character;
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.spawnedAt = spawnedAt;
        this.expiresAt = expiresAt;
        this.active = true;
    }

    public boolean isAvailableAt(LocalDateTime now) {
        return Boolean.TRUE.equals(active) && expiresAt.isAfter(now);
    }

    public void deactivate() {
        this.active = false;
    }

    public Long getId() {
        return id;
    }

    public CharacterInfo getCharacter() {
        return character;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public LocalDateTime getSpawnedAt() {
        return spawnedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public Boolean getActive() {
        return active;
    }
}