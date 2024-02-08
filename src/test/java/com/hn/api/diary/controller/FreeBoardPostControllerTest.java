package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostUpdateDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostWriteDTO;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.Member;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import jakarta.servlet.http.Cookie;
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
        long deletedPostId = freeBoardPosts.stream()
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
        Cookie[] cookies = (Cookie[]) map.get("cookies");

        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post1 = posts.get(0);
        FreeBoardPost post2 = posts.get(1);
        FreeBoardPost post3 = posts.get(2);
        FreeBoardPost post5 = posts.get(3);
        FreeBoardPost post6 = posts.get(4);

        // when
        ResultActions actions_1 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post1.getId())
                .cookie(cookies)
        );
        ResultActions actions_2 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post2.getId())
                .cookie(cookies)
        );

        ResultActions actions_3 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post3.getId())
                .cookie(cookies)
        );

        ResultActions actions_5 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post5.getId())
                .cookie(cookies)
        );

        ResultActions actions_6 = mockMvc.perform(MockMvcRequestBuilders.delete("/freeBoard/post/delete/" + post6.getId())
                .cookie(cookies)
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
        // given
        HashMap map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
        Member member = (Member) map.get("user");
        Cookie[] cookies = (Cookie[]) map.get("cookies");

        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post1 = freeBoardPosts.stream()
                .filter(p -> p.getMember().getId() == member.getId())
                .findFirst()
                .orElseThrow();
        FreeBoardPost post2 = freeBoardPosts.stream()
                .filter(p -> p.getMember().getId() != member.getId())
                .findFirst()
                .orElseThrow();

        FreeBoardPostUpdateDTO dto1 = FreeBoardPostUpdateDTO.builder()
                .postId(post1.getId().toString())
                .title("updateTitle")
                .content("updateContent")
                .build();
        FreeBoardPostUpdateDTO dto2 = FreeBoardPostUpdateDTO.builder()
                .postId(post2.getId().toString())
                .title("updateTitle")
                .content("updateContent")
                .build();

        String json1 = objectMapper.writeValueAsString(dto1);
        String json2 = objectMapper.writeValueAsString(dto2);

        // when
        ResultActions actions1 = mockMvc.perform(MockMvcRequestBuilders.put("/freeBoard/post/update")
                .cookie(cookies)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1)
        );

        ResultActions actions2 = mockMvc.perform(MockMvcRequestBuilders.put("/freeBoard/post/update")
                .cookie(cookies)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2)
        );

        // then
        actions1.andExpect(MockMvcResultMatchers.status().isOk());
        actions2.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("freeBoardPost - reply")
    void reply() throws Exception {
        // given
        Random random = new Random();

        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost freeBoardPost = freeBoardPosts.get(random.nextInt(freeBoardPosts.size()));

        HashMap<String, Object> map = new FreeBoardTestData().signIn(userRepository, objectMapper, mockMvc);
        Cookie[] cookies = (Cookie[]) map.get("cookies");

        FreeBoardPostReplyDTO freeBoardPostReplyDTO = FreeBoardPostReplyDTO.builder()
                .postId(freeBoardPost.getId().toString())
                .title("reply-title")
                .content("reply-content")
                .build();
        String reply_json = objectMapper.writeValueAsString(freeBoardPostReplyDTO);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/freeBoard/post/reply")
                .cookie(cookies)
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
        Cookie[] cookies = (Cookie[]) map.get("cookies");

        // [when]
        FreeBoardPostWriteDTO freeBoardPostWriteDTO = FreeBoardPostWriteDTO.builder()
                .title("title-write")
                .content("content-write")
                .build();
        String write_json = objectMapper.writeValueAsString(freeBoardPostWriteDTO);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/freeBoard/post/write")
                .cookie(cookies)
                .contentType(MediaType.APPLICATION_JSON)
                .content(write_json)
        );

        // [then]
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("freeBoardPost - getPosts")
    void getPosts() throws Exception {
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
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/freeBoard/posts/totalCount"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
