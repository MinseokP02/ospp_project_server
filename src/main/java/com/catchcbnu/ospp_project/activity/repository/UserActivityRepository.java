package com.catchcbnu.ospp_project.activity.repository;

import com.catchcbnu.ospp_project.activity.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Page<UserActivity> findByUser_IdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );
}