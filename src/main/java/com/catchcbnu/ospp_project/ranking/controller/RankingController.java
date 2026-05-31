package com.catchcbnu.ospp_project.ranking.controller;

import com.catchcbnu.ospp_project.common.response.ApiResponse;
import com.catchcbnu.ospp_project.ranking.dto.*;
import com.catchcbnu.ospp_project.ranking.service.RankingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<UserRankingPageResponse>> getUserRankings(
            @RequestParam(defaultValue = "ALL") String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserRankingPageResponse response = rankingService.getUserRankings(period, page, size);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "전체 사용자 랭킹 조회 성공", response)
        );
    }

    @GetMapping("/colleges")
    public ResponseEntity<ApiResponse<CollegeRankingListResponse>> getCollegeRankings(
            @RequestParam(defaultValue = "ALL") String period
    ) {
        CollegeRankingListResponse response = rankingService.getCollegeRankings(period);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "단과대 랭킹 조회 성공", response)
        );
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentRankingListResponse>> getDepartmentRankings(
            @RequestParam(required = false) String college,
            @RequestParam(defaultValue = "ALL") String period
    ) {
        DepartmentRankingListResponse response = rankingService.getDepartmentRankings(college, period);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "학과 랭킹 조회 성공", response)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyRankingResponse>> getMyRanking(
            @AuthenticationPrincipal Long userId
    ) {
        MyRankingResponse response = rankingService.getMyRanking(userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "내 랭킹 조회 성공", response)
        );
    }
}