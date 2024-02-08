package com.hn.api.diary.repository;

import com.hn.api.diary.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNick(String nick);
    Optional<Member> findByEmailOrNick(String email, String nick);

}
