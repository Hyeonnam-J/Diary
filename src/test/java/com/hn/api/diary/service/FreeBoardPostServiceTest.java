package com.hn.api.diary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.controller.FreeBoardTestData;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostWriteDTO;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class FreeBoardPostServiceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FreeBoardPostService freeBoardPostService;
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
    @DisplayName("freeBoardPost - read / success")
    void read_success() throws Exception {
        // given
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        int randNo = new Random().nextInt(freeBoardPosts.size());
        FreeBoardPost randPost = freeBoardPosts.get(randNo);
        long randPostId = randPost.getId();

        // when
        FreeBoardPost findPost = freeBoardPostRepository.findByIdWithNotDelete(randPostId);

        // then
        Assertions.assertEquals(randPost.getId(), findPost.getId());
    }

    @Test
    @DisplayName("freeBoardPost - read / invalidValue")
    void read_invalidValue() throws Exception {
        // given
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAll();
        long deletedPostId = freeBoardPosts.stream()
                .filter(FreeBoardPost::isDelete)
                .findFirst()
                .map(FreeBoardPost::getId)
                .orElse(0L);

        // expect
        Assertions.assertThrows(InvalidValue.class, () -> {
            FreeBoardPost freeBoardPost = freeBoardPostRepository.findByIdWithNotDelete(deletedPostId);
            if (freeBoardPost == null) {
                throw new InvalidValue();
            }
        });
    }

    @Test
    @DisplayName("freeBoardPost - delete")
    void delete() throws Exception {
        // given
        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post = posts.get( new Random().nextInt(posts.size()) );

        int countBeforeDelete = posts.size();

        // do
        post.setDelete(true);
        freeBoardPostRepository.save(post);

        // expect
        int countAfterDelete = freeBoardPostRepository.findAllWithNotDelete().size();
        Assertions.assertEquals(countBeforeDelete - 1, countAfterDelete);
    }

    @Test
    @DisplayName("freeBoardPost - update")
    void update() throws Exception {
        // given
        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post = posts.get( new Random().nextInt(posts.size()) );

        // do
        String originTitle = post.getTitle();
        String originContent = post.getContent();

        String updateTitle = "update-"+originTitle;
        String updateContent = "update-"+originContent;

        post.setTitle(updateTitle);
        post.setContent(updateContent);

        freeBoardPostRepository.save(post);

        // expect
        Assertions.assertEquals(updateTitle, post.getTitle());
        Assertions.assertEquals(updateContent, post.getContent());
        Assertions.assertNotEquals(originTitle, post.getTitle());
        Assertions.assertNotEquals(originContent, post.getContent());
    }

    @Test
    @DisplayName("freeBoardPost - reply")
    void reply() throws Exception {
        // given
        Random random = new Random();

        // 랜덤한 게시물
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost freeBoardPost = freeBoardPosts.get( random.nextInt(freeBoardPosts.size()) );
        // 랜덤한 게시물의 그룹
        List<FreeBoardPost> groupListBeforeReply = freeBoardPostRepository.findByGroupId(freeBoardPost.getGroupId());

        // 랜덤한 유저
        List<User> users = userRepository.findAll();
        User user = users.get( random.nextInt(users.size()) );

        FreeBoardPostReplyDTO freeBoardPostReplyDTO = FreeBoardPostReplyDTO.builder()
                .postId(freeBoardPost.getId().toString())
                .title("reply-title")
                .content("reply-content")
                .build();

        // when
        freeBoardPostService.reply(freeBoardPostReplyDTO, user.getId().toString());

        // then
        Assertions.assertEquals(groupListBeforeReply.size() + 1, freeBoardPostRepository.findByGroupId(freeBoardPost.getGroupId()).size() );
    }

    @Test
    @DisplayName("freeBoardPost - write")
    void write() throws Exception {
        // given
        List<User> users = userRepository.findAll();
        User user = users.get( new Random().nextInt(users.size()) );

        long countBeforeWrite = freeBoardPostRepository.count();

        FreeBoardPostWriteDTO freeBoardPostWriteDTO = FreeBoardPostWriteDTO.builder()
                .title("title-write")
                .content("content-write")
                .build();

        // when
        freeBoardPostService.write(freeBoardPostWriteDTO, user.getId().toString());

        // then
        Assertions.assertEquals(countBeforeWrite + 1, freeBoardPostRepository.count());
    }

    @Test
    @DisplayName("freeBoardPost - getPosts")
    void getPosts() throws Exception{
        Assertions.assertDoesNotThrow(() -> freeBoardPostService.getPosts(1, "basic"));
    }

    @Test
    @DisplayName("freeBoardPost - getTotalCount")
    void getTotalCount() throws Exception {
        Assertions.assertDoesNotThrow(() -> freeBoardPostService.getTotalCount());
    }
}
