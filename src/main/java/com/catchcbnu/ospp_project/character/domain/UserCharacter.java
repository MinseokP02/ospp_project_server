package com.catchcbnu.ospp_project.character.domain;

import com.catchcbnu.ospp_project.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_characters",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_character_spawn",
                        columnNames = {"user_id", "spawn_id"}
                )
        }
)
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id")
    private CharacterInfo character;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spawn_id")
    private CharacterSpawn spawn;

    @Column(name = "sensor_id", nullable = false)
    private Long sensorId;

    @Column(name = "sensor_name", nullable = false, length = 100)
    private String sensorName;

    @Column(name = "found_at", nullable = false)
    private LocalDateTime foundAt;

    protected UserCharacter() {
    }

    public UserCharacter(
            User user,
            CharacterInfo character,
            CharacterSpawn spawn,
            Long sensorId,
            String sensorName
    ) {
        this.user = user;
        this.character = character;
        this.spawn = spawn;
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.foundAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public CharacterInfo getCharacter() {
        return character;
    }

    public CharacterSpawn getSpawn() {
        return spawn;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public LocalDateTime getFoundAt() {
        return foundAt;
    }
}