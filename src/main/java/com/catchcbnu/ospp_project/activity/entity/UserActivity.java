package com.catchcbnu.ospp_project.activity.entity;

import com.catchcbnu.ospp_project.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activities")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private ActivityType type;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "exp_change", nullable = false)
    private Integer expChange;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected UserActivity() {
    }

    public UserActivity(
            User user,
            ActivityType type,
            String title,
            String description,
            Integer expChange
    ) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.description = description;
        this.expChange = expChange;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ActivityType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getExpChange() {
        return expChange;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}