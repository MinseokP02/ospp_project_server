package com.catchcbnu.ospp_project.submission.entity;

import com.catchcbnu.ospp_project.sensor.entity.Sensor;
import com.catchcbnu.ospp_project.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "submissions",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_sensor_time_slot",
                columnNames = {"user_id", "sensor_id", "time_slot"}
        )
)
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "temperature", precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "humidity", precision = 5, scale = 2)
    private BigDecimal humidity;

    @Column(name = "eco2")
    private Integer eco2;

    @Column(name = "air_quality")
    private Integer airQuality;

    @Column(name = "rssi")
    private Integer rssi;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "time_slot", nullable = false)
    private LocalDateTime timeSlot;

    @Column(name = "reward_exp", nullable = false)
    private Integer rewardExp;

    protected Submission() {
    }

    public Submission(
            User user,
            Sensor sensor,
            BigDecimal temperature,
            BigDecimal humidity,
            Integer eco2,
            Integer airQuality,
            Integer rssi,
            BigDecimal latitude,
            BigDecimal longitude,
            LocalDateTime measuredAt,
            LocalDateTime timeSlot,
            int rewardExp
    ) {
        this.user = user;
        this.sensor = sensor;
        this.temperature = temperature;
        this.humidity = humidity;
        this.eco2 = eco2;
        this.airQuality = airQuality;
        this.rssi = rssi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.measuredAt = measuredAt;
        this.submittedAt = LocalDateTime.now();
        this.timeSlot = timeSlot;
        this.rewardExp = rewardExp;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public Integer getEco2() {
        return eco2;
    }

    public Integer getAirQuality() {
        return airQuality;
    }

    public Integer getRssi() {
        return rssi;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getTimeSlot() {
        return timeSlot;
    }

    public Integer getRewardExp() {
        return rewardExp;
    }
}
