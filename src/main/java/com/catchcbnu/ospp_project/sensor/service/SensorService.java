package com.catchcbnu.ospp_project.sensor.service;

import com.catchcbnu.ospp_project.sensor.dto.LatestSensorDataResponse;
import com.catchcbnu.ospp_project.sensor.dto.SensorResponse;
import com.catchcbnu.ospp_project.sensor.entity.Sensor;
import com.catchcbnu.ospp_project.sensor.repository.SensorRepository;
import com.catchcbnu.ospp_project.submission.entity.Submission;
import com.catchcbnu.ospp_project.submission.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SubmissionRepository submissionRepository;

    public SensorService(SensorRepository sensorRepository, SubmissionRepository submissionRepository) {
        this.sensorRepository = sensorRepository;
        this.submissionRepository = submissionRepository;
    }

    @Transactional(readOnly = true)
    public List<SensorResponse> getSensors(boolean activeOnly) {
        List<Sensor> sensors = activeOnly ? sensorRepository.findByActiveTrue() : sensorRepository.findAll();
        return sensors.stream()
                .map(sensor -> toResponse(sensor, null))
                .toList();
    }

    @Transactional(readOnly = true)
    public SensorResponse getSensor(Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new IllegalArgumentException("센서를 찾을 수 없습니다."));
        Submission latestSubmission = submissionRepository.findTopBySensorIdOrderBySubmittedAtDesc(sensorId)
                .orElse(null);
        return toResponse(sensor, latestSubmission);
    }

    private SensorResponse toResponse(Sensor sensor, Submission latestSubmission) {
        LatestSensorDataResponse latestData = latestSubmission == null ? null : new LatestSensorDataResponse(
                latestSubmission.getTemperature(),
                latestSubmission.getHumidity(),
                latestSubmission.getEco2(),
                latestSubmission.getAirQuality(),
                latestSubmission.getRssi(),
                latestSubmission.getMeasuredAt(),
                latestSubmission.getSubmittedAt()
        );

        return new SensorResponse(
                sensor.getId(),
                sensor.getSensorName(),
                sensor.getLocationName(),
                sensor.getLatitude(),
                sensor.getLongitude(),
                sensor.getActive(),
                sensor.getUpdatedAt(),
                latestData
        );
    }
}
