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
    @Query("select c from Comment c where c.post.id = :postId and c.isDelete = false")
    List<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("select count(c) from Comment c where c.post.id = :postId and c.isDelete = false")
    long countByPostIdWithIsDelete(@Param("postId") Long postId);
}
