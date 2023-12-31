package com.hn.api.diary.repository;

import com.hn.api.diary.entity.FreeBoardPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long> {

    @Query("select p from FreeBoardPost p where p.id = :postId and p.isDelete = false")
    FreeBoardPost findByIdWithNotDelete(@Param("postId") Long postId);

    @Query("select count(p) from FreeBoardPost p where p.parentId = :parentId and p.isDelete = false")
    long countByParentIdWithNoDelete(@Param("parentId") Long parentId);

    @Query("select p from FreeBoardPost p where p.groupId = :groupId")
    List<FreeBoardPost> findByGroupId(@Param("groupId") Long groupId);

    @Query("select p from FreeBoardPost p where p.isDelete = false")
    List<FreeBoardPost> findAllWithNotDelete(Pageable pageable);

    @Query("select p from FreeBoardPost p where p.isDelete = false")
    List<FreeBoardPost> findAllWithNotDelete();

    @Query("select count(p) from FreeBoardPost p where p.isDelete = false")
    long getTotalCount();

}
