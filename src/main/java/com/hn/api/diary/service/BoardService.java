package com.hn.api.diary.service;

import com.hn.api.diary.dto.*;
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

    public BoardReadDTO read(Long postId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(InvalidValue::new);
        return new ModelMapper().map(board, BoardReadDTO.class);
    }

    public void delete(String userId, String postDetailId){
        // todo: delete -> put : isDelete true
        boardRepository.deleteById(Long.parseLong(postDetailId));
    }

    public void update(BoardUpdateDTO boardUpdateDTO, String userId, String postDetailId) {
        Board post = boardRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);
        // todo: lastModifiedDate
        post.setTitle(boardUpdateDTO.getTitle());
        post.setContent(boardUpdateDTO.getContent());

        boardRepository.save(post);
    }

    public void reply(BoardReplyDTO boardReplyDTO, String userId, String postDetailId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        Board originPost = boardRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);

        // 원글 아래 기존 게시물 num + 1
        List<Board> originPosts = boardRepository.findByOrigin(originPost.getOrigin());
        originPosts = originPosts.stream()
                .peek(board -> {
                    if (board.getNum() > originPost.getNum())
                        board.setNum(board.getNum() + 1);
                })
                .collect(Collectors.toList());
        boardRepository.saveAll(originPosts);

        Board board = Board.builder()
                .title(boardReplyDTO.getTitle())
                .content(boardReplyDTO.getContent())
                .user(user)
                .origin(originPost.getOrigin())
                .num(originPost.getNum() + 1)
                .depth(originPost.getDepth() + 1)
                .build();
        boardRepository.save(board);
    }

    public void write(BoardWriteDTO boardWriteDTO, String userId) {
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

    public List<BoardPostsDTO> getPosts(int page, String sort) {
        Pageable pageable;

        switch (sort) {
            case BoardSort.BASIC:
                pageable = PageRequest.of(page, BoardPageSize.BASIC,
                        Sort.by("origin").descending().and(Sort.by("num")));
                break;

            default:
                pageable = PageRequest.of(page, BoardPageSize.BASIC,
                        Sort.by("origin").descending().and(Sort.by("num")));
                break;
        }

        // todo: where isDelete is false
        Iterable<Board> iterablePosts = boardRepository.findAll(pageable);
        List<Board> posts = StreamSupport.stream(iterablePosts.spliterator(), false)
                .collect(Collectors.toList());

        // todo: N + 1 issue
        // for(Board b : posts){
        // System.out.println(b.getUser().getId());
        // System.out.println(b.getId());
        // }

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

    public int getTotalPostsCount() {
        return (int) boardRepository.count();
    }
}
