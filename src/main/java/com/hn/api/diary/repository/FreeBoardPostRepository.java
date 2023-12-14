package com.hn.api.diary.repository;

import com.hn.api.diary.entity.FreeBoardPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long> {

    @Query("select count(p) from FreeBoardPost p where p.origin = :origin and p.isDelete = false")
    long countByOriginWithNoDelete(@Param("origin") Long origin);

    @EntityGraph(attributePaths = {"freeBoardComments", "freeBoardComments.user"})
    @Query("select p from FreeBoardPost p where p.origin = :origin")
    List<FreeBoardPost> findByOrigin(@Param("origin") Long origin);

    @EntityGraph(attributePaths = {"freeBoardComments", "freeBoardComments.user"})
    @Query("select p from FreeBoardPost p where p.isDelete = false")
    List<FreeBoardPost> findAllWithComments(Pageable pageable);

    @Query("select count(p) from FreeBoardPost p where p.isDelete = false")
    long getTotalCount();

}
