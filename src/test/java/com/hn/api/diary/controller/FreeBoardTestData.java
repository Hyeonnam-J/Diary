package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.member.SignInDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.Member;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;

public class FreeBoardTestData {

    /**
     * user1: role is user.
     * user2: role is user.
     *
     * post_1: written by user1, two comment.
     * post_2: written by user1.
     * post_3: written by user1, one reply.
     * post_4: written by user1, isDelete true.
     * post_5: written by user2.
     * post_6: written by user1, reply post_3.
     *
     * comment_1: written by user1, post_1.
     * comment_2: written by user1, post_1, one reply.
     * comment_3: written by user1, post_1, isDelete true.
     * comment_4: written by user2, post_1.
     * comment_5: written by user1, post_1. reply comment_2.
     */
    public void given(MemberRepository memberRepository, FreeBoardPostRepository freeBoardPostRepository, FreeBoardCommentRepository freeBoardCommentRepository) {
        // user start ****
        Member member1 = Member.builder()
                .email("nami0879@naver.com")
                .password("!@#123QWEqwe")
                .role("user")
                .memberName("user1")
                .nick("user1")
                .build();

        Member member2 = Member.builder()
                .email("nami0878@naver.com")
                .password("!@#123QWEqwe")
                .role("user")
                .memberName("user2")
                .nick("user2")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        // user end ****

        // freeBoardPost start ****
        FreeBoardPost post1 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .member(member1)
                .build();
        freeBoardPostRepository.save(post1);
        post1.setGroupId(post1.getId());
        freeBoardPostRepository.save(post1);

        FreeBoardPost post2 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .member(member1)
                .build();
        freeBoardPostRepository.save(post2);
        post2.setGroupId(post2.getId());
        freeBoardPostRepository.save(post2);

        FreeBoardPost post3 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .member(member1)
                .build();
        freeBoardPostRepository.save(post3);
        post3.setGroupId(post3.getId());
        freeBoardPostRepository.save(post3);

        FreeBoardPost post4 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .member(member1)
                .build();
        freeBoardPostRepository.save(post4);
        post4.setGroupId(post4.getId());
        post4.setDelete(true);
        freeBoardPostRepository.save(post4);

        FreeBoardPost post5 = FreeBoardPost.builder()
                .title("title")
                .content("content")
                .member(member2)
                .build();
        freeBoardPostRepository.save(post5);
        post5.setGroupId(post5.getId());
        freeBoardPostRepository.save(post5);

        FreeBoardPost post6_replyPost3 = FreeBoardPost.builder()
                .title("title-reply")
                .content("content-reply")
                .groupId(post3.getGroupId())
                .groupNo(post3.getGroupNo() + 1)
                .depth(post3.getDepth() + 1)
                .parentId(post3.getId())
                .member(member1)
                .build();
        freeBoardPostRepository.save(post6_replyPost3);
        // freeBoardPost end ****

        // freeBoardComment start ****
        FreeBoardComment comment1 = FreeBoardComment.builder()
                .freeBoardPost(post1)
                .member(member1)
                .content("content")
                .isParent(true)
                .build();
        freeBoardCommentRepository.save(comment1);
        comment1.setGroupId(comment1.getId());
        freeBoardCommentRepository.save(comment1);

        FreeBoardComment comment2 = FreeBoardComment.builder()
                .freeBoardPost(post1)
                .member(member1)
                .content("content")
                .isParent(true)
                .build();
        freeBoardCommentRepository.save(comment2);
        comment2.setGroupId(comment2.getId());
        freeBoardCommentRepository.save(comment2);

        FreeBoardComment comment3 = FreeBoardComment.builder()
                .freeBoardPost(post1)
                .member(member1)
                .content("content")
                .isParent(true)
                .build();
        freeBoardCommentRepository.save(comment3);
        comment3.setGroupId(comment3.getId());
        comment3.setDelete(true);
        freeBoardCommentRepository.save(comment3);

        FreeBoardComment comment4 = FreeBoardComment.builder()
                .freeBoardPost(post1)
                .member(member2)
                .content("content")
                .isParent(true)
                .build();
        freeBoardCommentRepository.save(comment4);
        comment4.setGroupId(comment4.getId());
        freeBoardCommentRepository.save(comment4);

        FreeBoardComment comment5_replyComment2 = FreeBoardComment.builder()
                .freeBoardPost(post1)
                .member(member1)
                .content("content-reply")
                .isParent(false)
                .groupId(comment2.getGroupId())
                .build();
        freeBoardCommentRepository.save(comment5_replyComment2);
        // freeBoardComment end ****
    }

    /**
     * return user1.
     */
    public HashMap<String, Object> signIn(MemberRepository memberRepository, ObjectMapper objectMapper, MockMvc mockMvc) throws Exception {
        List<Member> members = memberRepository.findAll();
        Member member = members.get(0);

        SignInDTO signInDTO = SignInDTO.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
        String signIn_json = objectMapper.writeValueAsString(signInDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signIn_json)
        );

        Cookie[] Cookies = resultActions.andReturn().getResponse().getCookies();

        HashMap<String, Object> map = new HashMap<>();
        map.put("member", member);
        map.put("cookies", Cookies);

        return map;
    }
}
