package com.hn.api.diary.repository;

import com.hn.api.diary.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOrigin(Long origin);
}
