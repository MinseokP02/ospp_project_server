package com.catchcbnu.ospp_project.user.controller;

import com.catchcbnu.ospp_project.activity.dto.UserActivityPageResponse;
import com.catchcbnu.ospp_project.activity.service.UserActivityService;
import com.catchcbnu.ospp_project.common.response.ApiResponse;
import com.catchcbnu.ospp_project.user.dto.UserLevelResponse;
import com.catchcbnu.ospp_project.user.dto.UserResponse;
import com.catchcbnu.ospp_project.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserActivityService userActivityService;

    public UserController(UserService userService, UserActivityService userActivityService) {
        this.userService = userService;
        this.userActivityService = userActivityService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @AuthenticationPrincipal Long userId
    ) {
        UserResponse response = userService.getUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "내 정보 조회 성공", response)
        );
    }

    @GetMapping("/me/level")
    public ResponseEntity<ApiResponse<UserLevelResponse>> getMyLevel(
            @AuthenticationPrincipal Long userId
    ) {
        UserLevelResponse response = userService.getMyLevel(userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "레벨 정보 조회 성공", response)
        );
    }

    @GetMapping("/me/activities")
    public ResponseEntity<ApiResponse<UserActivityPageResponse>> getMyActivities(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserActivityPageResponse response =
                userActivityService.getMyActivities(userId, page, size);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "활동 기록 조회 성공", response)
        );
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable Long userId
    ) {
        UserResponse response = userService.getUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "사용자 정보 조회 성공", response)
        );
    }
}
