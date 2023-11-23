package com.hn.api.diary.service;

import com.hn.api.diary.dto.PostBoardDTO;
import com.hn.api.diary.entity.Board;
import com.hn.api.diary.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public void post(PostBoardDTO postBoardDTO, Long userId){
        System.out.println(postBoardDTO.getTitle());
        System.out.println(postBoardDTO.getContent());
        System.out.println(userId);
    }

    public List<Board> getAllPosts(){
        List<Board> posts = boardRepository.findAll();
        return posts;
    }
}
