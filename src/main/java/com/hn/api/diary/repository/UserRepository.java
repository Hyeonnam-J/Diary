package com.hn.api.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hn.api.diary.entity.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
}
