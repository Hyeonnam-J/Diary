package com.hn.api.diary.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.exception.Forbidden;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hn.api.diary.dto.freeBoard.FreeBoardPostsDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReadDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostUpdateDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostWriteDTO;
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
    private final FreeBoardCommentRepository freeBoardCommentRepository;
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

    public void delete(String postId){
        // 코멘트가 있으면 삭제할 수 없다.
        long countComments = freeBoardCommentRepository.countByFreeBoardPostIdWithNoDelete(Long.parseLong(postId));
        if(countComments != 0) throw new Forbidden();

        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(Long.parseLong(postId))
                .orElseThrow(InvalidValue::new);

        // 원글일 경우에만,
        if(freeBoardPost.getId() == freeBoardPost.getOrigin()){
            long countReplies = freeBoardPostRepository.countByOriginWithNoDelete(freeBoardPost.getOrigin());
            // 1개 이상, 즉 자기 자신과 다른 글-답글이 있으면 삭제할 수 없다.
            if(countReplies > 1) throw new Forbidden();
        }

        freeBoardPost.setDelete(true);
        freeBoardPostRepository.save(freeBoardPost);
    }

    public void update(FreeBoardPostUpdateDTO freeBoardPostUpdateDTO) {
        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(Long.parseLong(freeBoardPostUpdateDTO.getPostId()))
                .orElseThrow(InvalidValue::new);

        freeBoardPost.setTitle(freeBoardPostUpdateDTO.getTitle());
        freeBoardPost.setContent(freeBoardPostUpdateDTO.getContent());

        freeBoardPostRepository.save(freeBoardPost);
    }

    public void reply(FreeBoardPostReplyDTO freeBoardPostReplyDTO, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        FreeBoardPost originFreeBoardPost = freeBoardPostRepository.findById(Long.parseLong(freeBoardPostReplyDTO.getPostId()))
                .orElseThrow(InvalidValue::new);

        FreeBoardPost freeBoardPost = FreeBoardPost.builder()
                .title(freeBoardPostReplyDTO.getTitle())
                .content(freeBoardPostReplyDTO.getContent())
                .user(user)
                .origin(originFreeBoardPost.getOrigin())
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
                        Sort.by("origin").descending().and(Sort.by("createdDate").ascending()));
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
