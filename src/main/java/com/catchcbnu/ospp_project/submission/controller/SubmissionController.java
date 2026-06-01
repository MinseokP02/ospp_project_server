package com.catchcbnu.ospp_project.submission.controller;

import com.catchcbnu.ospp_project.common.response.ApiResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionAvailabilityResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionPageResponse;
import com.catchcbnu.ospp_project.submission.dto.SubmissionRequest;
import com.catchcbnu.ospp_project.submission.dto.SubmissionResponse;
import com.catchcbnu.ospp_project.submission.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/availability")
    public ApiResponse<SubmissionAvailabilityResponse> getAvailability(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long sensorId
    ) {
        SubmissionAvailabilityResponse response = submissionService.getAvailability(userId, sensorId);
        return ApiResponse.success(HttpStatus.OK, "전송 가능 여부 조회 성공", response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SubmissionResponse> submit(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody SubmissionRequest request
    ) {
        System.out.println("===submission controller 진입===");
        System.out.println("UserId = " + userId);
        System.out.println("request=" + request);
        SubmissionResponse response = submissionService.submit(userId, request);

        System.out.println("=== Submission Service 완료 ---");

        return ApiResponse.success(HttpStatus.CREATED, "센서 데이터 전송 성공", response);
    }

    @GetMapping("/me")
    public ApiResponse<SubmissionPageResponse> getMySubmissions(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SubmissionPageResponse response =
                submissionService.getMySubmissions(userId, page, size);

        return ApiResponse.success(
                HttpStatus.OK,
                "내 전송 기록 조회 성공",
                response
        );
    }
}
