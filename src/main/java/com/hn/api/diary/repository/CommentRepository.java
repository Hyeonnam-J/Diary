package com.hn.api.diary.repository;

import com.hn.api.diary.entity.Comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user", "post"})
    @Query("select c from Comment c where c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);

    long countByPostId(Long postId);
}
