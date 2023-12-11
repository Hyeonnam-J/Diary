package com.hn.api.diary;

import java.util.List;
import java.util.ArrayList;

import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    FreeBoardPostRepository freeBoardPostRepository;
    @Autowired
    FreeBoardCommentRepository freeBoardCommentRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = userRepository.findById(1L).orElseThrow();

        List<FreeBoardPost> freeBoardPosts = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            FreeBoardPost freeBoardPost = FreeBoardPost.builder()
                    .title(i + "")
                    .content(i + "")
                    .user(user)
                    .build();
            freeBoardPost.setOrigin(freeBoardPost.getId());
            freeBoardPosts.add(freeBoardPost);
        }
        freeBoardPostRepository.saveAll(freeBoardPosts);

        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(1L).orElseThrow();
        List<FreeBoardComment> freeBoardComments = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            FreeBoardComment freeBoardComment = FreeBoardComment.builder()
                    .freeBoardPost(freeBoardPost)
                    .content(i+"")
                    .user(user)
                    .build();
            freeBoardComments.add(freeBoardComment);
        }
        freeBoardCommentRepository.saveAll(freeBoardComments);
    }
}
