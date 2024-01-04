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
import java.util.Random;

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
        List<FreeBoardComment> comments = freeBoardCommentRepository.findAllWithNotDelete();
        FreeBoardComment comment = comments.get( new Random().nextInt(comments.size()) );
        int countBeforeDelete = comments.size();

        // when
        comment.setDelete(true);
        freeBoardCommentRepository.save(comment);

        // then
        Assertions.assertEquals(countBeforeDelete - 1, freeBoardCommentRepository.findAllWithNotDelete().size());
    }

    @Test
    void update() {
        // given
        List<FreeBoardComment> comments = freeBoardCommentRepository.findAllWithNotDelete();
        FreeBoardComment comment = comments.get( new Random().nextInt(comments.size()) );
        String originContent = comment.getContent();
        String updateContent = "update-"+originContent;

        // when
        comment.setContent(updateContent);
        freeBoardCommentRepository.save(comment);

        // then
        Assertions.assertNotEquals(originContent, comment.getContent());
        Assertions.assertEquals(updateContent, comment.getContent());
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