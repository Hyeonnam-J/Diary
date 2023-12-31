package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostUpdateDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostWriteDTO;
import com.hn.api.diary.dto.user.SignInDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@AutoConfigureMockMvc
@SpringBootTest
public class FreeBoardPostControllerTest {

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
        given();
    }

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

    HashMap<String, Object> signIn() throws Exception {
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

    @Test
    @DisplayName("freeBoardPost - read / success")
    void read_success() throws Exception {
        // [given]
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        int randNo = new Random().nextInt(freeBoardPosts.size());
        FreeBoardPost freeBoardPost = freeBoardPosts.get(randNo);
        long randPostId = freeBoardPost.getId();

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/post/read/" + randPostId));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("freeBoardPost - read / invalidValue")
    void read_invalidValue() throws Exception {
        // [given]
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAll();
        long deletedPostId= freeBoardPosts.stream()
                .filter(FreeBoardPost::isDelete)
                .findFirst()
                .map(FreeBoardPost::getId)
                .orElse(0L);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/post/read/" + deletedPostId));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
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
        ResultActions actions_1 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + freeBoardPost_1.getId()));
        ResultActions actions_2 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + freeBoardPost_2.getId()));
        ResultActions actions_3 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + freeBoardPost_3.getId()));

        // [then]
        actions_1.andExpect(MockMvcResultMatchers.status().isForbidden());
        actions_2.andExpect(MockMvcResultMatchers.status().isOk());
        actions_3.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("freeBoardPost - update")
    void update() throws Exception {
        // [given]
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost freeBoardPost = freeBoardPosts.get( new Random().nextInt(freeBoardPosts.size()) );

        // [when]
        FreeBoardPostUpdateDTO freeBoardPostUpdateDTO = FreeBoardPostUpdateDTO.builder()
                .postId(freeBoardPost.getId().toString())
                .title("updateTitle")
                .content("updateContent")
                .build();
        String json = objectMapper.writeValueAsString(freeBoardPostUpdateDTO);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/freeBoard/post/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        );

        // [then]
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("freeBoardPost - reply")
    void reply() throws Exception {
        // [given]
        Random random = new Random();

        // 랜덤한 게시물
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost freeBoardPost = freeBoardPosts.get( random.nextInt(freeBoardPosts.size()) );
        // 랜덤한 게시물의 그룹
        List<FreeBoardPost> groupListBeforeReply = freeBoardPostRepository.findByGroupId(freeBoardPost.getGroupId());

        HashMap<String, Object> map = signIn();
        User user = (User) map.get("user");
        String token = (String) map.get("token");

        // [when]
        FreeBoardPostReplyDTO freeBoardPostReplyDTO = FreeBoardPostReplyDTO.builder()
                .postId(freeBoardPost.getId().toString())
                .title("reply-title")
                .content("reply-content")
                .build();
        String reply_json = objectMapper.writeValueAsString(freeBoardPostReplyDTO);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/freeBoard/post/reply")
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reply_json)
        );

        // [then]
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("freeBoardPost - write")
    void write() throws Exception {
        // [given]
        HashMap<String, Object> map = signIn();
        User user = (User) map.get("user");
        String token = (String) map.get("token");

        long postCountBeforeWrite = freeBoardPostRepository.count();

        // [when]
        FreeBoardPostWriteDTO freeBoardPostWriteDTO = FreeBoardPostWriteDTO.builder()
                .title("title-write")
                .content("content-write")
                .build();
        String write_json = objectMapper.writeValueAsString(freeBoardPostWriteDTO);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/freeBoard/post/write")
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(write_json)
        );

        // [then]
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("freeBoardPost - getPosts")
    void getPosts() throws Exception{
        // do
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/posts")
                .param("page", "1")
                .param("sort", "basic")
        );

        // expect
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("freeBoardPost - getTotalCount")
    void getTotalCount() throws Exception {
        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/posts/totalCount"));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
