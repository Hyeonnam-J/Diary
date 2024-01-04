package com.hn.api.diary.repository;

import com.hn.api.diary.entity.FreeBoardComment;

import com.hn.api.diary.entity.FreeBoardPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {

    @Query("select c from FreeBoardComment c where c.id = :commentId and c.isDelete = false")
    FreeBoardComment findByIdWithNotDelete(@Param("commentId") Long commentId);

    @Query("select count(c) from FreeBoardComment c where c.groupId = :groupId and c.isDelete = false")
    long countByGroupIdWithNoDelete(@Param("groupId") Long groupId);

    @Query("select count(c) from FreeBoardComment c where c.freeBoardPost.id = :postId and c.isDelete = false")
    long countByFreeBoardPostIdWithNoDelete(@Param("postId") Long postId);

    @EntityGraph(attributePaths = {"user", "freeBoardPost"})
    @Query("select c from FreeBoardComment c where c.freeBoardPost.id = :postId and c.isDelete = false")
    List<FreeBoardComment> findByFreeBoardPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("select count(c) from FreeBoardComment c where c.freeBoardPost.id = :postId and c.isDelete = false")
    long getTotalCount(@Param("postId") Long postId);
}
