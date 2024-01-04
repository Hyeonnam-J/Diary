package com.hn.api.diary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.controller.FreeBoardTestData;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.Forbidden;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FreeBoardCommentServiceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FreeBoardCommentService freeBoardCommentService;
    @Autowired
    private FreeBoardPostRepository freeBoardPostRepository;
    @Autowired
    private FreeBoardCommentRepository freeBoardCommentRepository;

    @BeforeEach
    void clean(){
        freeBoardCommentRepository.deleteAll();
        freeBoardPostRepository.deleteAll();
        userRepository.deleteAll();
        new FreeBoardTestData().given(userRepository, freeBoardPostRepository, freeBoardCommentRepository);
    }

    @Test
    void delete() {
        // given
        List<User> users = userRepository.findAll();
        User user = users.get(0);

        List<FreeBoardComment> comments = freeBoardCommentRepository.findAllWithNotDelete();
        FreeBoardComment comment1 = comments.get(0);
        FreeBoardComment comment2 = comments.get(1);
        FreeBoardComment comment4 = comments.get(2);
        FreeBoardComment comment5 = comments.get(3);

        // expect
        Assertions.assertDoesNotThrow(() -> {
            freeBoardCommentService.delete(comment1.getId().toString(), user.getId().toString());
        });

        Assertions.assertThrows(Forbidden.class, () -> {
            freeBoardCommentService.delete(comment2.getId().toString(), user.getId().toString());
        });

        Assertions.assertThrows(Forbidden.class, () -> {
            freeBoardCommentService.delete(comment4.getId().toString(), user.getId().toString());
        });

        Assertions.assertDoesNotThrow(() -> {
            freeBoardCommentService.delete(comment5.getId().toString(), user.getId().toString());
        });
    }

    @Test
    void update() {
    }

    @Test
    void write() {
    }

    @Test
    void reply() {
    }

    @Test
    void getComments() {
    }

    @Test
    void getTotalCount() {
    }
}