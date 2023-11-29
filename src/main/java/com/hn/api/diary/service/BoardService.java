package com.hn.api.diary.service;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.BoardReadDTO;
import com.hn.api.diary.dto.BoardReplyDTO;
import com.hn.api.diary.dto.BoardWriteDTO;
import com.hn.api.diary.entity.Board;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.BoardRepository;
import com.hn.api.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private class BoardPageSize {
        private static final int BASIC = 10;
    }

    private class BoardSort {
        private static final String BASIC = "basic";
    }

    public BoardReadDTO read(Long postId){
        Board board = boardRepository.findById(postId)
                .orElseThrow(InvalidValue::new);
        return new ModelMapper().map(board, BoardReadDTO.class);
    }
    
    public void reply(BoardReplyDTO boardReplyDTO, String userId, String postDetailId){
        System.out.println(boardReplyDTO.getTitle());
        System.out.println(boardReplyDTO.getContent());
        System.out.println(userId);
        System.out.println(postDetailId);
        // User user = userRepository.findById(Long.parseLong(userId))
        //         .orElseThrow(InvalidValue::new);

        // Board board = Board.builder()
        //         .title(boardWriteDTO.getTitle())
        //         .content(boardWriteDTO.getContent())
        //         .user(user)
        //         .build();

        // boardRepository.save(board);

        // board.setOrigin(board.getId());
        // boardRepository.save(board);
    }

    public void write(BoardWriteDTO boardWriteDTO, String userId){
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        Board board = Board.builder()
                .title(boardWriteDTO.getTitle())
                .content(boardWriteDTO.getContent())
                .user(user)
                .build();

        boardRepository.save(board);

        board.setOrigin(board.getId());
        boardRepository.save(board);
    }

    public List<BoardPostsDTO> getPosts(int page, String sort){
        Pageable pageable;

        switch (sort) {
            case BoardSort.BASIC:
                pageable = PageRequest.of(page, BoardPageSize.BASIC, Sort.by("origin").descending().and(Sort.by("num")));
                break;
        
            default:
                pageable = PageRequest.of(page, BoardPageSize.BASIC, Sort.by("origin").descending().and(Sort.by("num")));
                break;
        }
        
        Iterable<Board> iterablePosts = boardRepository.findAll(pageable);
        List<Board> posts = StreamSupport.stream(iterablePosts.spliterator(), false)
                .collect(Collectors.toList());

        // todo: N + 1 issue
//        for(Board b : posts){
//            System.out.println(b.getUser().getId());
//            System.out.println(b.getId());
//        }

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

    public int getTotalPostsCount(){
        return (int) boardRepository.count();
    }
}
