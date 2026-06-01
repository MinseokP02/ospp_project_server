package com.catchcbnu.ospp_project.ranking.service;

import com.catchcbnu.ospp_project.ranking.dto.*;
import com.catchcbnu.ospp_project.user.entity.User;
import com.catchcbnu.ospp_project.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final UserRepository userRepository;

    public RankingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserRankingPageResponse getUserRankings(String period, int page, int size) {
        List<User> users = userRepository.findAllByOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc();

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        int totalElements = users.size();
        int start = Math.min(safePage * safeSize, totalElements);
        int end = Math.min(start + safeSize, totalElements);

        List<UserRankingResponse> rankings = new ArrayList<>();

        for (int i = start; i < end; i++) {
            User user = users.get(i);

            rankings.add(new UserRankingResponse(
                    i + 1,
                    user.getId(),
                    user.getNickname(),
                    user.getCollege(),
                    user.getDepartment(),
                    user.getTotalSubmissionCount(),
                    user.getExp(),
                    user.getLevel(),
                    user.getUpdatedAt()
            ));
        }

        return new UserRankingPageResponse(
                safePage,
                safeSize,
                (long) totalElements,
                rankings
        );
    }

    @Transactional(readOnly = true)
    public CollegeRankingListResponse getCollegeRankings(String period) {
        List<User> users = userRepository.findAll();

        Map<String, List<User>> groupedByCollege = users.stream()
                .collect(Collectors.groupingBy(User::getCollege));

        List<CollegeRankingResponse> result = groupedByCollege.entrySet()
                .stream()
                .map(entry -> {
                    String college = entry.getKey();
                    List<User> collegeUsers = entry.getValue();

                    long totalSubmissionCount = collegeUsers.stream()
                            .mapToLong(User::getTotalSubmissionCount)
                            .sum();

                    long userCount = collegeUsers.size();

                    return new CollegeRankingTemp(
                            college,
                            totalSubmissionCount,
                            userCount
                    );
                })
                .sorted(
                        Comparator.comparingLong(CollegeRankingTemp::totalSubmissionCount).reversed()
                                .thenComparing(
                                        Comparator.comparingLong(CollegeRankingTemp::userCount).reversed()
                                )
                                .thenComparing(CollegeRankingTemp::college)
                )
                .map(temp -> new CollegeRankingResponse(
                        0,
                        temp.college(),
                        temp.totalSubmissionCount(),
                        temp.userCount()
                ))
                .toList();

        List<CollegeRankingResponse> rankedResult = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            CollegeRankingResponse item = result.get(i);

            rankedResult.add(new CollegeRankingResponse(
                    i + 1,
                    item.college(),
                    item.totalSubmissionCount(),
                    item.userCount()
            ));
        }

        return new CollegeRankingListResponse(rankedResult);
    }

    @Transactional(readOnly = true)
    public DepartmentRankingListResponse getDepartmentRankings(String college, String period) {
        List<User> users;

        if (college == null || college.isBlank()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findByCollegeOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(college);
        }

        Map<DepartmentKey, List<User>> groupedByDepartment = users.stream()
                .collect(Collectors.groupingBy(
                        user -> new DepartmentKey(user.getCollege(), user.getDepartment())
                ));

        List<DepartmentRankingResponse> result = groupedByDepartment.entrySet()
                .stream()
                .map(entry -> {
                    DepartmentKey key = entry.getKey();
                    List<User> departmentUsers = entry.getValue();

                    long totalSubmissionCount = departmentUsers.stream()
                            .mapToLong(User::getTotalSubmissionCount)
                            .sum();

                    long userCount = departmentUsers.size();

                    return new DepartmentRankingTemp(
                            key.college(),
                            key.department(),
                            totalSubmissionCount,
                            userCount
                    );
                })
                .sorted(
                Comparator.comparingLong(DepartmentRankingTemp::totalSubmissionCount).reversed()
                        .thenComparing(
                                Comparator.comparingLong(DepartmentRankingTemp::userCount).reversed()
                        )
                        .thenComparing(DepartmentRankingTemp::college)
                        .thenComparing(DepartmentRankingTemp::department)
                )
                .map(temp -> new DepartmentRankingResponse(
                        0,
                        temp.college(),
                        temp.department(),
                        temp.totalSubmissionCount(),
                        temp.userCount()
                ))
                .toList();

        List<DepartmentRankingResponse> rankedResult = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            DepartmentRankingResponse item = result.get(i);

            rankedResult.add(new DepartmentRankingResponse(
                    i + 1,
                    item.college(),
                    item.department(),
                    item.totalSubmissionCount(),
                    item.userCount()
            ));
        }

        return new DepartmentRankingListResponse(rankedResult);
    }

    @Transactional(readOnly = true)
    public MyRankingResponse getMyRanking(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        int overallRank = findRank(
                user.getId(),
                userRepository.findAllByOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc()
        );

        int collegeRank = findRank(
                user.getId(),
                userRepository.findByCollegeOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(
                        user.getCollege()
                )
        );

        int departmentRank = findRank(
                user.getId(),
                userRepository.findByCollegeAndDepartmentOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(
                        user.getCollege(),
                        user.getDepartment()
                )
        );

        return new MyRankingResponse(
                overallRank,
                collegeRank,
                departmentRank,
                user.getTotalSubmissionCount(),
                user.getExp(),
                user.getLevel()
        );
    }

    private int findRank(Long userId, List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                return i + 1;
            }
        }

        return 0;
    }

    private record CollegeRankingTemp(
            String college,
            long totalSubmissionCount,
            long userCount
    ) {
    }

    private record DepartmentKey(
            String college,
            String department
    ) {
    }

    private record DepartmentRankingTemp(
            String college,
            String department,
            long totalSubmissionCount,
            long userCount
    ) {
    }
}