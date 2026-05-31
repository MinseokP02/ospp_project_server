package com.catchcbnu.ospp_project.character.repository;

import com.catchcbnu.ospp_project.character.domain.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<CharacterInfo, Long> {

    List<CharacterInfo> findAllByOrderByIdAsc();
}