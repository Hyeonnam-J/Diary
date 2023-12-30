package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReadDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import com.hn.api.diary.response.PlainDataResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@AutoConfigureMockMvc
@SpringBootTest
public class FreeBoardPostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private FreeBoardPostRepository freeBoardPostRepository;
    @Autowired private FreeBoardCommentRepository freeBoardCommentRepository;

    @BeforeEach
    void clean() {
        freeBoardCommentRepository.deleteAll();
        freeBoardPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    HashMap<String, Object> given() {
        HashMap<String, Object> map = new HashMap<>();

        // user start ****
        List<User> userList = new ArrayList<>();

        User user1 = User.builder()
                .email("nami0879@naver.com")
                .password("!@#123QWEqwe")
                .role("user")
                .userName("jhn")
                .nick("hn")
                .build();

        User user2 = User.builder()
                .email("nami0878@naver.com")
                .password("!@#123QWEqwe")
                .role("user")
                .userName("jhn")
                .nick("hn")
                .build();

        userList.add(user1);
        userList.add(user2);

        userRepository.saveAll(userList);
        map.put("userList", userList);
        // user end ****

        // freeBoardPost start ****
        List<FreeBoardPost> freeBoardPostList = new ArrayList<>();

        FreeBoardPost freeBoardPost1 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .user(user1)
                .build();

        FreeBoardPost freeBoardPost2 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .user(user1)
                .build();

        freeBoardPostList.add(freeBoardPost1);
        freeBoardPostList.add(freeBoardPost2);

        freeBoardPostRepository.saveAll(freeBoardPostList);
        map.put("freeBoardPostList", freeBoardPostList);
        // freeBoardPost end ****

        // freeBoardComment start ****
        List<FreeBoardComment> freeBoardCommentList = new ArrayList<>();

        FreeBoardComment freeBoardComment1 = FreeBoardComment.builder()
                .freeBoardPost(freeBoardPost1)
                .user(user1)
                .content("content")
                .build();

        FreeBoardComment freeBoardComment2 = FreeBoardComment.builder()
                .freeBoardPost(freeBoardPost1)
                .user(user1)
                .content("content")
                .build();

        freeBoardCommentList.add(freeBoardComment1);
        freeBoardCommentList.add(freeBoardComment2);

        freeBoardCommentRepository.saveAll(freeBoardCommentList);
        map.put("freeBoardCommentList", freeBoardCommentList);
        // freeBoardComment end ****

        return map;
    }

    @Test
    @DisplayName("freeBoardPost - read")
    void read() throws Exception {
        // [given]
        HashMap<String, Object> map = given();
        List<User> userList = (List) map.get("userList");
        List<FreeBoardPost> freeBoardPostList = (List) map.get("freeBoardPostList");
        List<FreeBoardComment> freeBoardCommentList = (List) map.get("freeBoardCommentList");

        // [when]
        int randNo = new Random().nextInt(freeBoardPostList.size());
        FreeBoardPost randFreeBoardPost = freeBoardPostList.get(randNo);
        long randPostId = randFreeBoardPost.getId();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/post/read/"+randPostId));

        String json = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(json);

        String data = jsonNode.get("data").toString();
        FreeBoardPostReadDTO freeBoardPostReadDTO = objectMapper.readValue(data, FreeBoardPostReadDTO.class);

        // [then]
        Assertions.assertEquals(randPostId, freeBoardPostReadDTO.getId());
    }

}
