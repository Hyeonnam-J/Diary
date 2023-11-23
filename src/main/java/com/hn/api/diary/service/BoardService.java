package com.hn.api.diary.service;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.PostBoardDTO;
import com.hn.api.diary.entity.Board;
import com.hn.api.diary.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public void post(PostBoardDTO postBoardDTO, Long userId){
        System.out.println(postBoardDTO.getTitle());
        System.out.println(postBoardDTO.getContent());
        System.out.println(userId);
    }

    public List<BoardPostsDTO> getAllPosts(){
        List<Board> posts = boardRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd.");

        return posts.stream()
                .map(p -> {
                    BoardPostsDTO dto = modelMapper.map(p, BoardPostsDTO.class);
                    String strCreatedDate = p.getCreatedDate().format(formatter);
                    dto.setCreatedDate(strCreatedDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
