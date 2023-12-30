package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReadDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostUpdateDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        given();
    }

    /**
     * user1: role is user.
     * user2: role is user.
     *
     * post_1: two comment.
     * post_2: nothing.
     * post_3: one reply.
     *
     * user1 write all posts and comments
     */
    void given() {
        // user start ****
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

        userRepository.save(user1);
        userRepository.save(user2);
        // user end ****

        // freeBoardPost start ****
        FreeBoardPost freeBoardPost1 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .user(user1)
                .build();
        freeBoardPostRepository.save(freeBoardPost1);
        freeBoardPost1.setGroupId(freeBoardPost1.getId());
        freeBoardPostRepository.save(freeBoardPost1);

        FreeBoardPost freeBoardPost2 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .user(user1)
                .build();
        freeBoardPostRepository.save(freeBoardPost2);
        freeBoardPost2.setGroupId(freeBoardPost2.getId());
        freeBoardPostRepository.save(freeBoardPost2);

        FreeBoardPost freeBoardPost3 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .user(user1)
                .build();
        freeBoardPostRepository.save(freeBoardPost3);
        freeBoardPost3.setGroupId(freeBoardPost3.getId());
        freeBoardPostRepository.save(freeBoardPost3);

        FreeBoardPost freeBoardPost3_reply = FreeBoardPost.builder()
                .title("title-reply")
                .content("content-reply")
                .groupId(freeBoardPost3.getGroupId())
                .groupNo(freeBoardPost3.getGroupNo()+1)
                .depth(freeBoardPost3.getDepth()+1)
                .parentId(freeBoardPost3.getId())
                .user(user1)
                .build();
        freeBoardPostRepository.save(freeBoardPost3_reply);
        // freeBoardPost end ****

        // freeBoardComment start ****
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

        freeBoardCommentRepository.save(freeBoardComment1);
        freeBoardCommentRepository.save(freeBoardComment2);
        // freeBoardComment end ****
    }

    @Test
    @DisplayName("freeBoardPost - read")
    void read() throws Exception {
        // [given]
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAll();
        int randNo = new Random().nextInt(freeBoardPosts.size());
        FreeBoardPost freeBoardPost = freeBoardPosts.get(randNo);
        long randPostId = freeBoardPost.getId();

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/post/read/"+randPostId));

        String json = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(json);

        String data = jsonNode.get("data").toString();
        FreeBoardPostReadDTO freeBoardPostReadDTO = objectMapper.readValue(data, FreeBoardPostReadDTO.class);

        // [then]
        Assertions.assertEquals(randPostId, freeBoardPostReadDTO.getId());
    }

    @Test
    @DisplayName("freeBoardPost - delete")
    void delete() throws Exception {
        // [given]
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAll();
        FreeBoardPost freeBoardPost_1 = freeBoardPosts.get(0);
        FreeBoardPost freeBoardPost_2 = freeBoardPosts.get(1);
        FreeBoardPost freeBoardPost_3 = freeBoardPosts.get(2);

        // [when]
        mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/"+freeBoardPost_1.getId()));
        mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/"+freeBoardPost_2.getId()));
        mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/"+freeBoardPost_3.getId()));

        // [then]
        FreeBoardPost afterDeleteFreeBoardPost_1 = freeBoardPostRepository.findById(freeBoardPost_1.getId()).orElseThrow();
        FreeBoardPost afterDeleteFreeBoardPost_2 = freeBoardPostRepository.findById(freeBoardPost_2.getId()).orElseThrow();
        FreeBoardPost afterDeleteFreeBoardPost_3 = freeBoardPostRepository.findById(freeBoardPost_3.getId()).orElseThrow();

        Assertions.assertEquals(afterDeleteFreeBoardPost_1.isDelete(), false);
        Assertions.assertEquals(afterDeleteFreeBoardPost_2.isDelete(), true);
        Assertions.assertEquals(afterDeleteFreeBoardPost_3.isDelete(), false);
    }

    @Test
    @DisplayName("freeBoardPost - update")
    void update() throws Exception{
        // [given]
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAll();
        int randNo = new Random().nextInt(freeBoardPosts.size());
        FreeBoardPost freeBoardPost = freeBoardPosts.get(randNo);

        Long postId = freeBoardPost.getId();
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";

        // [when]
        FreeBoardPostUpdateDTO freeBoardPostUpdateDTO = FreeBoardPostUpdateDTO.builder()
                .postId(postId.toString())
                .title(updateTitle)
                .content(updateContent)
                .build();
        String json = objectMapper.writeValueAsString(freeBoardPostUpdateDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/freeBoard/post/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        );

        // [then]
        FreeBoardPost freeBoardPostAfterUpdate = freeBoardPostRepository.findById(postId).orElseThrow();

        Assertions.assertEquals(updateTitle, freeBoardPostAfterUpdate.getTitle());
        Assertions.assertEquals(updateContent, freeBoardPostAfterUpdate.getContent());
    }
}
