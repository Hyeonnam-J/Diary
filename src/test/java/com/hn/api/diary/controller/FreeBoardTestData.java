package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.user.SignInDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FreeBoardTestData {

    /**
     * user1: role is user.
     * user2: role is user.
     *
     * post_1: two comment.
     * post_2: nothing.
     * post_3: one reply.
     * post_4: isDelete true.
     *
     * user1 write all posts and comments
     */
    public void given(UserRepository userRepository, FreeBoardPostRepository freeBoardPostRepository, FreeBoardCommentRepository freeBoardCommentRepository) {
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
                .groupNo(freeBoardPost3.getGroupNo() + 1)
                .depth(freeBoardPost3.getDepth() + 1)
                .parentId(freeBoardPost3.getId())
                .user(user1)
                .build();
        freeBoardPostRepository.save(freeBoardPost3_reply);

        FreeBoardPost freeBoardPost4 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .user(user1)
                .build();
        freeBoardPostRepository.save(freeBoardPost4);
        freeBoardPost4.setGroupId(freeBoardPost4.getId());
        freeBoardPost4.setDelete(true);
        freeBoardPostRepository.save(freeBoardPost4);
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

    public HashMap<String, Object> signIn(UserRepository userRepository, ObjectMapper objectMapper, MockMvc mockMvc) throws Exception {
        List<User> users = userRepository.findAll();
        User user = users.get( new Random().nextInt(users.size()) );

        SignInDTO signInDTO = SignInDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        String signIn_json = objectMapper.writeValueAsString(signInDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signIn_json)
        );
        String token = resultActions.andReturn().getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        HashMap<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);

        return map;
    }
}
