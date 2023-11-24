package com.hn.api.diary;

import com.hn.api.diary.entity.Board;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    BoardRepository boardRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = User.builder()
                .email("user")
                .password("user")
                .role("USER")
                .build();

        User admin = User.builder()
                .email("admin")
                .password("admin")
                .role("ADMIN")
                .build();

        List<Board> list = IntStream.range(0, 47)
                .mapToObj(i -> {
                    return Board.builder()
                            .title("title"+i)
                            .content("content"+i)
                            .createdDat(LocalDateTime.now())
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());
        boardRepository.saveAll(list);

        List<Board> list2 = IntStream.range(47, 91)
                .mapToObj(i -> {
                    return Board.builder()
                            .title("title"+i)
                            .content("content"+i)
                            .createdDat(LocalDateTime.now())
                            .user(admin)
                            .build();
                })
                .collect(Collectors.toList());
        boardRepository.saveAll(list2);
    }
}
