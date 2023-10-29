package com.hn.api.diary.repository;

import com.hn.api.diary.domain.Sign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignRepository extends JpaRepository<Sign, Long> {
}
