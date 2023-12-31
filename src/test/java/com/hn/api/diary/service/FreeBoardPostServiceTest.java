package com.hn.api.diary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostWriteDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.Forbidden;
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

    @Test
    @DisplayName("freeBoardPost - read / success")
    void read_success() throws Exception {
        // do
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAllWithNotDelete();
        int randNo = new Random().nextInt(freeBoardPosts.size());
        FreeBoardPost randPost = freeBoardPosts.get(randNo);
        long randPostId = randPost.getId();

        FreeBoardPost findPost = freeBoardPostRepository.findByIdWithNotDelete(randPostId);

        // expect
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
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findAll();
        FreeBoardPost freeBoardPost_1 = freeBoardPosts.get(0);
        FreeBoardPost freeBoardPost_2 = freeBoardPosts.get(1);
        FreeBoardPost freeBoardPost_3 = freeBoardPosts.get(2);

        // expect
        Assertions.assertThrows(Forbidden.class, () -> {
            freeBoardPostService.delete(freeBoardPost_1.getId().toString());
        });

        Assertions.assertDoesNotThrow(() -> {
            freeBoardPostService.delete(freeBoardPost_2.getId().toString());
        });

        Assertions.assertThrows(Forbidden.class, () -> {
            freeBoardPostService.delete(freeBoardPost_3.getId().toString());
        });
    }

    @Test
    @DisplayName("freeBoardPost - update")
    void update() throws Exception {
        // do
        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost postBeforeUpdate = posts.get( new Random().nextInt(posts.size()) );

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";

        postBeforeUpdate.setTitle(updateTitle);
        postBeforeUpdate.setContent(updateContent);

        freeBoardPostRepository.save(postBeforeUpdate);

        // expect
        FreeBoardPost postAfterUpdate = freeBoardPostRepository.findByIdWithNotDelete(postBeforeUpdate.getId());

        Assertions.assertEquals(updateTitle, postAfterUpdate.getTitle());
        Assertions.assertEquals(updateContent, postAfterUpdate.getContent());
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
        // do
        List<User> users = userRepository.findAll();
        User user = users.get( new Random().nextInt(users.size()) );

        long countBeforeWrite = freeBoardPostRepository.count();

        FreeBoardPostWriteDTO freeBoardPostWriteDTO = FreeBoardPostWriteDTO.builder()
                .title("title-write")
                .content("content-write")
                .build();

        freeBoardPostService.write(freeBoardPostWriteDTO, user.getId().toString());

        // expect
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
