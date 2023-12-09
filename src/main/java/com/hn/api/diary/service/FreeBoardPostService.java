package com.hn.api.diary.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hn.api.diary.dto.FreeBoardPostsDTO;
import com.hn.api.diary.dto.FreeBoardPostReadDTO;
import com.hn.api.diary.dto.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.FreeBoardPostUpdateDTO;
import com.hn.api.diary.dto.FreeBoardPostWriteDTO;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FreeBoardPostService {

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final UserRepository userRepository;

    private class BoardPageSize {
        private static final int BASIC = 10;
    }

    private class BoardSort {
        private static final String BASIC = "basic";
    }

    public FreeBoardPostReadDTO read(Long postId) {
        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(postId)
                .orElseThrow(InvalidValue::new);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");
        String formattedCreatedDate = freeBoardPost.getCreatedDate().format(dateTimeFormatter);

        FreeBoardPostReadDTO dto = new ModelMapper().map(freeBoardPost, FreeBoardPostReadDTO.class);
        dto.setCreatedDate(formattedCreatedDate);

        return dto;
    }

    public void delete(String userId, String postId){
        // todo: when deleted origin post..
        // todo: think! Hierarchical board...
        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(Long.parseLong(postId))
                .orElseThrow(InvalidValue::new);

        freeBoardPost.setDelete(true);
        freeBoardPostRepository.save(freeBoardPost);
    }

    public void update(FreeBoardPostUpdateDTO freeBoardPostUpdateDTO, String userId, String postDetailId) {
        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);

        freeBoardPost.setTitle(freeBoardPostUpdateDTO.getTitle());
        freeBoardPost.setContent(freeBoardPostUpdateDTO.getContent());

        freeBoardPostRepository.save(freeBoardPost);
    }

    public void reply(FreeBoardPostReplyDTO freeBoardPostReplyDTO, String userId, String postDetailId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        FreeBoardPost originFreeBoardPost = freeBoardPostRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);

        // 원글 아래 기존 게시물 num + 1
        List<FreeBoardPost> originFreeBoardPosts = freeBoardPostRepository.findByOrigin(originFreeBoardPost.getOrigin());
        originFreeBoardPosts = originFreeBoardPosts.stream()
                .peek(board -> {
                    if (board.getNum() > originFreeBoardPost.getNum())
                        board.setNum(board.getNum() + 1);
                })
                .collect(Collectors.toList());
        freeBoardPostRepository.saveAll(originFreeBoardPosts);

        FreeBoardPost freeBoardPost = FreeBoardPost.builder()
                .title(freeBoardPostReplyDTO.getTitle())
                .content(freeBoardPostReplyDTO.getContent())
                .user(user)
                .origin(originFreeBoardPost.getOrigin())
                .num(originFreeBoardPost.getNum() + 1)
                .depth(originFreeBoardPost.getDepth() + 1)
                .build();
        freeBoardPostRepository.save(freeBoardPost);
    }

    public void write(FreeBoardPostWriteDTO freeBoardPostWriteDTO, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        FreeBoardPost freeBoardPost = FreeBoardPost.builder()
                .title(freeBoardPostWriteDTO.getTitle())
                .content(freeBoardPostWriteDTO.getContent())
                .user(user)
                .build();

        freeBoardPostRepository.save(freeBoardPost);

        freeBoardPost.setOrigin(freeBoardPost.getId());
        freeBoardPostRepository.save(freeBoardPost);
    }

    public List<FreeBoardPostsDTO> getPosts(int page, String sort) {
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

        Iterable<FreeBoardPost> iterablePosts = freeBoardPostRepository.findAllWithComments(pageable);
        List<FreeBoardPost> freeBoardPosts = StreamSupport.stream(iterablePosts.spliterator(), false)
                .collect(Collectors.toList());

        ModelMapper modelMapper = new ModelMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd.");

        return freeBoardPosts.stream()
                .map(p -> {
                    FreeBoardPostsDTO dto = modelMapper.map(p, FreeBoardPostsDTO.class);
                    String strCreatedDate = p.getCreatedDate().format(formatter);
                    dto.setCreatedDate(strCreatedDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public int getTotalCount() {
        return (int) freeBoardPostRepository.getTotalCount();
    }
}
