package com.hn.api.diary.repository;

import com.hn.api.diary.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOrigin(Long origin);
}
