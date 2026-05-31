package com.catchcbnu.ospp_project.character.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "characters")
public class CharacterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "character_name", nullable = false, length = 100)
    private String name;

    @Column(name = "rarity", nullable = false, length = 50)
    private String rarity;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "base_spawn_rate", nullable = false)
    private Double baseSpawnRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected CharacterInfo() {
    }

    public CharacterInfo(String name, String rarity, String description, Double baseSpawnRate) {
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.baseSpawnRate = baseSpawnRate;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }

    public String getDescription() {
        return description;
    }

    public Double getBaseSpawnRate() {
        return baseSpawnRate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}