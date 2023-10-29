package com.hn.api.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hn.api.diary.entity.Sign;

public interface SignRepository extends JpaRepository<Sign, Long> {
}
