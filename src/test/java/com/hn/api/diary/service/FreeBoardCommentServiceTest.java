package com.hn.api.diary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.controller.FreeBoardTestData;
import com.hn.api.diary.dto.freeBoard.FreeBoardCommentReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardCommentWriteDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.Member;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
class FreeBoardCommentServiceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
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
        memberRepository.deleteAll();
        new FreeBoardTestData().given(memberRepository, freeBoardPostRepository, freeBoardCommentRepository);
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
        //given
        Random random = new Random();

        List<Member> members = memberRepository.findAll();
        Member member = members.get( random.nextInt(members.size()) );

        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post = posts.get( random.nextInt(posts.size()) );

        long countBeforeWrite = freeBoardCommentRepository.count();

        FreeBoardCommentWriteDTO dto = FreeBoardCommentWriteDTO.builder()
                .postId(post.getId().toString())
                .content("write")
                .build();

        // when
        freeBoardCommentService.write(dto, member.getId().toString());

        // then
        Assertions.assertEquals(countBeforeWrite + 1, freeBoardCommentRepository.count());
    }

    @Test
    void reply() {
        // given
        List<Member> members = memberRepository.findAll();
        Member member = members.get( new Random().nextInt(members.size()) );

        List<FreeBoardComment> comments = freeBoardCommentRepository.findAllWithNotDelete();
        FreeBoardComment comment = comments.stream()
                .filter(c -> c.isParent() == true)
                .findFirst()
                .orElseThrow();

        long countBeforeReply = freeBoardCommentRepository.countByGroupIdWithNoDelete(comment.getGroupId());

        FreeBoardCommentReplyDTO dto = FreeBoardCommentReplyDTO.builder()
                .commentId(comment.getId().toString())
                .content("reply")
                .build();

        // when
        freeBoardCommentService.reply(dto, member.getId().toString());

        // then
        Assertions.assertEquals(countBeforeReply + 1, freeBoardCommentRepository.countByGroupIdWithNoDelete(comment.getGroupId()));
    }

    @Test
    void getComments() {
        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post = posts.get( new Random().nextInt(posts.size()) );

        Assertions.assertDoesNotThrow(() -> freeBoardCommentService.getComments(post.getId(), 1));
    }

    @Test
    void getTotalCount() {
        List<FreeBoardPost> posts = freeBoardPostRepository.findAllWithNotDelete();
        FreeBoardPost post = posts.get( new Random().nextInt(posts.size()) );

        Assertions.assertDoesNotThrow(() -> freeBoardCommentService.getTotalCount(post.getId()));
    }
}