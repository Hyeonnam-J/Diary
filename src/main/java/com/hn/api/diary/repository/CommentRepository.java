package com.hn.api.diary.repository;

import com.hn.api.diary.entity.Comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId, Pageable pageable);
    long countByPostId(Long postId);
}
