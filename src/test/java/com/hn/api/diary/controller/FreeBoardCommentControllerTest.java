package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.freeBoard.FreeBoardCommentUpdateDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class FreeBoardCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FreeBoardPostRepository freeBoardPostRepository;
    @Autowired
    private FreeBoardCommentRepository freeBoardCommentRepository;

    @BeforeEach
    void clean() {
        freeBoardCommentRepository.deleteAll();
        freeBoardPostRepository.deleteAll();
        userRepository.deleteAll();
        new FreeBoardTestData().given(userRepository, freeBoardPostRepository, freeBoardCommentRepository);
    }

    @Test
    void delete() throws Exception {
        // given
        HashMap map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
        User user = (User) map.get("user");
        String token = (String) map.get("token");

        List<FreeBoardComment> comments = freeBoardCommentRepository.findAllWithNotDelete();
        FreeBoardComment comment1 = comments.get(0);
        FreeBoardComment comment2 = comments.get(1);
        FreeBoardComment comment4 = comments.get(2);
        FreeBoardComment comment5 = comments.get(3);

        ResultActions actions_1 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/comment/delete/"+comment1.getId())
                .header("userId", String.valueOf(user.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        ResultActions actions_2 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/comment/delete/"+comment2.getId())
                .header("userId", String.valueOf(user.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        ResultActions actions_4 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/comment/delete/"+comment4.getId())
                .header("userId", String.valueOf(user.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        ResultActions actions_5 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/comment/delete/"+comment5.getId())
                .header("userId", String.valueOf(user.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        // then
        actions_1.andExpect(MockMvcResultMatchers.status().isOk());
        actions_2.andExpect(MockMvcResultMatchers.status().isForbidden());
        actions_4.andExpect(MockMvcResultMatchers.status().isForbidden());
        actions_5.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void update() throws Exception {
        // given
        HashMap map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
        User user = (User) map.get("user");
        String token = (String) map.get("token");

        List<FreeBoardComment> comments = freeBoardCommentRepository.findAllWithNotDelete();
        Long commentId_1 = comments.stream()
                .filter(c -> c.getUser().getId() == user.getId())
                .findFirst()
                .map(FreeBoardComment::getId)
                .orElseThrow();
        Long commentId_2 = comments.stream()
                .filter(c -> c.getUser().getId() != user.getId())
                .findFirst()
                .map(FreeBoardComment::getId)
                .orElseThrow();

        FreeBoardCommentUpdateDTO dto1 = FreeBoardCommentUpdateDTO.builder()
                .commentId(commentId_1.toString())
                .content("update")
                .build();
        FreeBoardCommentUpdateDTO dto2 = FreeBoardCommentUpdateDTO.builder()
                .commentId(commentId_2.toString())
                .content("update")
                .build();

        String json1 = objectMapper.writeValueAsString(dto1);
        String json2 = objectMapper.writeValueAsString(dto2);

        // when
        ResultActions actions1 = mockMvc.perform(MockMvcRequestBuilders.put("/freeBoard/comment/update")
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1)
        );
        ResultActions actions2 = mockMvc.perform(MockMvcRequestBuilders.put("/freeBoard/comment/update")
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2)
        );

        // then
        actions1.andExpect(MockMvcResultMatchers.status().isOk());
        actions2.andExpect(MockMvcResultMatchers.status().isForbidden());
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