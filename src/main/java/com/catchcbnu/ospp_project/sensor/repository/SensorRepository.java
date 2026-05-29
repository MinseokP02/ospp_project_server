package com.catchcbnu.ospp_project.sensor.repository;

import com.catchcbnu.ospp_project.sensor.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByActiveTrue();
}
