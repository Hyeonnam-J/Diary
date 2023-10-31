package com.hn.api.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hn.api.diary.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
