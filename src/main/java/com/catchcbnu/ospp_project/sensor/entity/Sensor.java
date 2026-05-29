package com.catchcbnu.ospp_project.sensor.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
@EntityListeners(AuditingEntityListener.class)
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_name", nullable = false, unique = true, length = 100)
    private String sensorName;

    @Column(name = "location_name", length = 100)
    private String locationName;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Sensor() {
    }

    public Sensor(String sensorName, String locationName, BigDecimal latitude, BigDecimal longitude) {
        this.sensorName = sensorName;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getLocationName() {
        return locationName;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
