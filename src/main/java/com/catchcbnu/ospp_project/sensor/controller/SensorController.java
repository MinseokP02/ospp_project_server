package com.catchcbnu.ospp_project.sensor.controller;

import com.catchcbnu.ospp_project.common.response.ApiResponse;
import com.catchcbnu.ospp_project.sensor.dto.SensorResponse;
import com.catchcbnu.ospp_project.sensor.service.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public ApiResponse<List<SensorResponse>> getSensors(
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        return ApiResponse.success(HttpStatus.OK, "센서 목록 조회 성공", sensorService.getSensors(activeOnly));
    }

    @GetMapping("/{sensorId}")
    public ApiResponse<SensorResponse> getSensor(@PathVariable Long sensorId) {
        return ApiResponse.success(HttpStatus.OK, "센서 상세 조회 성공", sensorService.getSensor(sensorId));
    }
}
