package com.catchcbnu.ospp_project.user.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "college", nullable = false, length = 100)
    private String college;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "exp", nullable = false)
    private Integer exp;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "total_submission_count", nullable = false)
    private Integer totalSubmissionCount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected User() {
    }

    public User(String email, String passwordHash, String nickname, String college, String department) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.college = college;
        this.department = department;
        this.exp = 0;
        this.level = 1;
        this.totalSubmissionCount = 0;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCollege() {
        return college;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getExp() {
        return exp;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getTotalSubmissionCount() {
        return totalSubmissionCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void addExp(int amount) {
        this.exp += amount;
    }

    public void decreaseExp(int amount) {
        this.exp -= amount;
    }

    public void levelUp() {
        this.level += 1;
    }

    public void increaseSubmissionCount() {
        this.totalSubmissionCount += 1;
    }

    public void updateLevel(int newLevel) {
        this.level = newLevel;
    }
}
