package com.hn.api.diary.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.hn.api.diary.dto.BoardCommentReplyDTO;
import com.hn.api.diary.dto.BoardCommentUpdateDTO;
import com.hn.api.diary.dto.BoardCommentWriteDTO;
import com.hn.api.diary.entity.Post;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.PostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hn.api.diary.dto.BoardCommentsDTO;
import com.hn.api.diary.entity.Comment;
import com.hn.api.diary.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private class CommentPageSize {
        private static final int BASIC = 10;
    }

    public void boardCommentDelete(String commentId){
        // todo: when deleted origin comment..
        Comment comment = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(InvalidValue::new);

        comment.setDelete(true);
        commentRepository.save(comment);
    }

    public void boardCommentUpdate(BoardCommentUpdateDTO boardCommentUpdateDTO, String userId, String postDetailId, String commentId){
        Comment comment = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(InvalidValue::new);

        comment.setContent(boardCommentUpdateDTO.getContent());
        commentRepository.save(comment);
    }

    public void boardCommentWirte(BoardCommentWriteDTO boardCommentWriteDTO, String userId, String postDetailId){
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        Post post = postRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(boardCommentWriteDTO.getContent())
                .build();

        commentRepository.save(comment);

        comment.setOrigin(comment.getId());
        commentRepository.save(comment);
    }

    public void boardCommentReply(BoardCommentReplyDTO boardCommentReplyDTO, String userId, String postDetailId, String commentId){
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        Post post = postRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);

        Comment comment = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(InvalidValue::new);

        Comment comment_save = Comment.builder()
                .user(user)
                .post(post)
                .origin(comment.getOrigin())
                .content(boardCommentReplyDTO.getContent())
                .depth(1)
                .build();

        commentRepository.save(comment_save);
    }

    public List<BoardCommentsDTO> getBoardComments(Long postId, int page){
        Pageable pageable = PageRequest.of(page, CommentPageSize.BASIC,
                Sort.by("origin").ascending().and(Sort.by("createdDate").ascending()));

        Iterable<Comment> iterableComments = commentRepository.findByPostId(postId, pageable);

        List<Comment> comments = StreamSupport.stream(iterableComments.spliterator(), false)
                .collect(Collectors.toList());

        ModelMapper modelMapper = new ModelMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");

        return comments.stream()
                .map(c -> {
                    BoardCommentsDTO dto = modelMapper.map(c, BoardCommentsDTO.class);
                    String strCreatedDate = c.getCreatedDate().format(formatter);
                    dto.setCreatedDate(strCreatedDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public int getTotalCommentsCount(Long postId){
        return (int) commentRepository.countByPostIdWithIsDelete(postId);
    }
}
