package com.hn.api.diary.repository;

import com.hn.api.diary.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"comments", "comments.user"})
    @Query("select p from Post p where p.origin = :origin")
    List<Post> findByOrigin(@Param("origin") Long origin);

    @EntityGraph(attributePaths = {"comments", "comments.user"})
    @Query("select p from Post p")
    List<Post> findAllWithComments(Pageable pageable);

}
