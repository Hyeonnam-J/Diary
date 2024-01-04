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
        FreeBoardPost freeBoardPost = freeBoardPostRepository.findByIdWithNotDelete(postId);
        if(freeBoardPost == null) throw new InvalidValue();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");
        String formattedCreatedDate = freeBoardPost.getCreatedDate().format(dateTimeFormatter);

        FreeBoardPostReadDTO dto = new ModelMapper().map(freeBoardPost, FreeBoardPostReadDTO.class);
        dto.setCreatedDate(formattedCreatedDate);

        return dto;
    }

    public void delete(String postId, String userId){
        // 코멘트가 있으면 삭제할 수 없다.
        long countComments = freeBoardCommentRepository.countByFreeBoardPostIdWithNoDelete(Long.parseLong(postId));
        if(countComments != 0) throw new Forbidden();

        // 답글이 있으면 삭제할 수 없다.
        long countChildPosts = freeBoardPostRepository.countByParentIdWithNoDelete(Long.parseLong(postId));
        if(countChildPosts != 0) throw new Forbidden();

        FreeBoardPost freeBoardPost = freeBoardPostRepository.findByIdWithNotDelete(Long.parseLong(postId));
        if(freeBoardPost == null) throw new InvalidValue();

        // 글쓴이가 아니면 삭제할 수 없다.
        if( freeBoardPost.getUser().getId() != Long.parseLong(userId) ) throw new Forbidden();

        freeBoardPost.setDelete(true);
        freeBoardPostRepository.save(freeBoardPost);
    }

    public void update(FreeBoardPostUpdateDTO freeBoardPostUpdateDTO, String userId) {
        FreeBoardPost freeBoardPost = freeBoardPostRepository.findByIdWithNotDelete(Long.parseLong(freeBoardPostUpdateDTO.getPostId()));
        if(freeBoardPost == null) throw new InvalidValue();

        if((freeBoardPost.getUser().getId() != Long.parseLong(userId))) throw new Forbidden();

        freeBoardPost.setTitle(freeBoardPostUpdateDTO.getTitle());
        freeBoardPost.setContent(freeBoardPostUpdateDTO.getContent());

        freeBoardPostRepository.save(freeBoardPost);
    }

    public void reply(FreeBoardPostReplyDTO freeBoardPostReplyDTO, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        FreeBoardPost originFreeBoardPost = freeBoardPostRepository.findByIdWithNotDelete(Long.parseLong(freeBoardPostReplyDTO.getPostId()));
        if(originFreeBoardPost == null) throw new InvalidValue();

        // 답글의 대상 글 num 이후의 post에 num + 1
        List<FreeBoardPost> freeBoardPosts = freeBoardPostRepository.findByGroupId(originFreeBoardPost.getGroupId());
        freeBoardPosts.stream()
                .peek(p -> {
                    if(p.getGroupNo() > originFreeBoardPost.getGroupNo()) p.setGroupNo(p.getGroupNo()+1);
                })
                .collect(Collectors.toList());
        freeBoardPostRepository.saveAll(freeBoardPosts);

        FreeBoardPost freeBoardPost = FreeBoardPost.builder()
                .title(freeBoardPostReplyDTO.getTitle())
                .content(freeBoardPostReplyDTO.getContent())
                .user(user)
                .groupId(originFreeBoardPost.getGroupId())
                .groupNo(originFreeBoardPost.getGroupNo()+1)
                .depth(originFreeBoardPost.getDepth()+1)
                .parentId(originFreeBoardPost.getId())
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

        freeBoardPost.setGroupId(freeBoardPost.getId());
        freeBoardPostRepository.save(freeBoardPost);
    }

    public List<FreeBoardPostsDTO> getPosts(int page, String sort) {
        Pageable pageable;

        switch (sort) {
            case BoardSort.BASIC:
                pageable = PageRequest.of(page, BoardPageSize.BASIC,
                        Sort.by("groupId").descending()
                                .and(Sort.by("groupNo").ascending()));
                break;

            default:
                pageable = PageRequest.of(page, BoardPageSize.BASIC,
                        Sort.by("groupId").descending().and(Sort.by("groupNo")));
                break;
        }

        Iterable<FreeBoardPost> iterablePosts = freeBoardPostRepository.findAllWithNotDelete(pageable);
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
