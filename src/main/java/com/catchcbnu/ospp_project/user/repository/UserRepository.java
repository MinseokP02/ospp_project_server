package com.catchcbnu.ospp_project.user.repository;

import com.catchcbnu.ospp_project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByLevelDescExpDesc();

    List<User> findByCollegeOrderByLevelDescExpDesc(String college);

    List<User> findByDepartmentOrderByLevelDescExpDesc(String department);

    List<User> findAllByOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc();

    List<User> findByCollegeOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(String college);

    List<User> findByCollegeAndDepartmentOrderByTotalSubmissionCountDescExpDescUpdatedAtDesc(
            String college,
            String department
    );
}
