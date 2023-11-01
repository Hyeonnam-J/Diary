package com.hn.api.diary.repository;

import com.hn.api.diary.entity.MySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<MySession, Long> {

    Optional<MySession> findByToken(String token);

}
