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
        new FreeBoardTestData().given(userRepository, freeBoardPostRepository, freeBoardCommentRepository);
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
        // given
        HashMap map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
        User user = (User) map.get("user");
        String token = (String) map.get("token");

        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post1 = posts.get(0);
        FreeBoardPost post2 = posts.get(1);
        FreeBoardPost post3 = posts.get(2);
        FreeBoardPost post5 = posts.get(3);
        FreeBoardPost post6 = posts.get(4);

        // when
        ResultActions actions_1 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post1.getId())
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        );
        ResultActions actions_2 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post2.getId())
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        ResultActions actions_3 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post3.getId())
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        ResultActions actions_5 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post5.getId())
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        ResultActions actions_6 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post6.getId())
                .header("userId", user.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        // then
        actions_1.andExpect(MockMvcResultMatchers.status().isForbidden());
        actions_2.andExpect(MockMvcResultMatchers.status().isOk());
        actions_3.andExpect(MockMvcResultMatchers.status().isForbidden());
        actions_5.andExpect(MockMvcResultMatchers.status().isForbidden());
        actions_6.andExpect(MockMvcResultMatchers.status().isOk());
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

        HashMap<String, Object> map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
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
        HashMap<String, Object> map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
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
