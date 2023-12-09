package com.hn.api.diary.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.hn.api.diary.dto.FreeBoardBoardCommentReplyDTO;
import com.hn.api.diary.dto.FreeBoardCommentUpdateDTO;
import com.hn.api.diary.dto.FreeBoardCommentWriteDTO;
import com.hn.api.diary.entity.FreeBoardComment;
import com.hn.api.diary.entity.FreeBoardPost;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hn.api.diary.dto.FreeBoardCommentsDTO;
import com.hn.api.diary.repository.FreeBoardCommentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FreeBoardCommentService {

    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final FreeBoardPostRepository freeBoardPostRepository;
    private final UserRepository userRepository;

    private class CommentPageSize {
        private static final int BASIC = 10;
    }

    public void delete(String commentId){
        // todo: when deleted origin comment..
        FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(InvalidValue::new);

        freeBoardComment.setDelete(true);
        freeBoardCommentRepository.save(freeBoardComment);
    }

    public void update(FreeBoardCommentUpdateDTO freeBoardCommentUpdateDTO){
        FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(Long.parseLong(freeBoardCommentUpdateDTO.getCommentId()))
                .orElseThrow(InvalidValue::new);

        freeBoardComment.setContent(freeBoardCommentUpdateDTO.getContent());
        freeBoardCommentRepository.save(freeBoardComment);
    }

    public void write(FreeBoardCommentWriteDTO freeBoardCommentWriteDTO, String userId){
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        FreeBoardPost freeBoardPost = freeBoardPostRepository.findById(Long.parseLong(freeBoardCommentWriteDTO.getPostId()))
                .orElseThrow(InvalidValue::new);

        FreeBoardComment freeBoardComment = FreeBoardComment.builder()
                .freeBoardPost(freeBoardPost)
                .user(user)
                .content(freeBoardCommentWriteDTO.getContent())
                .build();

        freeBoardCommentRepository.save(freeBoardComment);

        freeBoardComment.setOrigin(freeBoardComment.getId());
        freeBoardCommentRepository.save(freeBoardComment);
    }

    public void reply(FreeBoardBoardCommentReplyDTO boardCommentReplyDTO, String userId){
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(Long.parseLong(boardCommentReplyDTO.getCommentId()))
                .orElseThrow(InvalidValue::new);

        FreeBoardComment freeBoardComment_save = FreeBoardComment.builder()
                .user(user)
                .freeBoardPost(freeBoardComment.getFreeBoardPost())
                .origin(freeBoardComment.getOrigin())
                .content(boardCommentReplyDTO.getContent())
                .depth(1)
                .build();

        freeBoardCommentRepository.save(freeBoardComment_save);
    }

    public List<FreeBoardCommentsDTO> getComments(Long postId, int page){
        Pageable pageable = PageRequest.of(page, CommentPageSize.BASIC,
                Sort.by("origin").ascending().and(Sort.by("createdDate").ascending()));

        Iterable<FreeBoardComment> iterableComments = freeBoardCommentRepository.findByFreeBoardPostId(postId, pageable);

        List<FreeBoardComment> freeBoardComments = StreamSupport.stream(iterableComments.spliterator(), false)
                .collect(Collectors.toList());

        ModelMapper modelMapper = new ModelMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");

        return freeBoardComments.stream()
                .map(c -> {
                    FreeBoardCommentsDTO dto = modelMapper.map(c, FreeBoardCommentsDTO.class);
                    String strCreatedDate = c.getCreatedDate().format(formatter);
                    dto.setCreatedDate(strCreatedDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public int getTotalCount(Long postId){
        return (int) freeBoardCommentRepository.getTotalCount(postId);
    }
}
