package com.hn.api.diary;

import com.hn.api.diary.repository.PostRepository;
import com.hn.api.diary.repository.CommentRepository;
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
    PostRepository postRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // User user = User.builder()
        //         .email("user")
        //         .password("user")
        //         .role("USER")
        //         .build();

        // User admin = User.builder()
        //         .email("admin")
        //         .password("admin")
        //         .role("ADMIN")
        //         .build();

        // List<Board> list = IntStream.range(1, 47)
        //         .mapToObj(i -> {
        //             return Board.builder()
        //                     .title("title" + i)
        //                     .content("content" + i)
        //                     .user(user)
        //                     .origin((long) i)
        //                     .num(0)
        //                     .depth(0)
        //                     .build();
        //         })
        //         .collect(Collectors.toList());
        // boardRepository.saveAll(list);

        // List<Board> list2 = IntStream.range(47, 91)
        //         .mapToObj(i -> {
        //             return Board.builder()
        //                     .title("title" + i)
        //                     .content("content" + i)
        //                     .user(admin)
        //                     .origin((long) i)
        //                     .num(0)
        //                     .depth(0)
        //                     .build();
        //         })
        //         .collect(Collectors.toList());
        // boardRepository.saveAll(list2);

        // User re = User.builder()
        //         .email("re")
        //         .password("re")
        //         .role("USER")
        //         .build();

        // Board b = Board.builder()
        //         .title("title 답글")
        //         .content("content 답글")
        //         .user(re)
        //         .origin(85L)
        //         .num(3)
        //         .depth(1)
        //         .build();
        // boardRepository.save(b);

        // User re2 = User.builder()
        //         .email("re2")
        //         .password("re2")
        //         .role("USER")
        //         .build();

        // Board b2 = Board.builder()
        //         .title("title 답글2")
        //         .content("content 답글2")
        //         .user(re2)
        //         .origin(85L)
        //         .num(2)
        //         .depth(1)
        //         .build();
        // boardRepository.save(b2);

        // User re3 = User.builder()
        //         .email("re3")
        //         .password("re3")
        //         .role("USER")
        //         .build();

        // Board b3 = Board.builder()
        //         .title("title 답글3")
        //         .content("content 답글3")
        //         .user(re3)
        //         .origin(85L)
        //         .num(1)
        //         .depth(1)
        //         .build();
        // boardRepository.save(b3);
    }
}
