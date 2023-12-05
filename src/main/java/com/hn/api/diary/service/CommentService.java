package com.hn.api.diary.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hn.api.diary.dto.BoardCommentsDTO;
import com.hn.api.diary.entity.Comment;
import com.hn.api.diary.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private class CommentPageSize {
        private static final int BASIC = 10;
    }

    public List<BoardCommentsDTO> getComments(Long postId, int page){
        Pageable pageable = PageRequest.of(page, CommentPageSize.BASIC);
        Iterable<Comment> iterableComments = commentRepository.findByPostId(postId, pageable);

        List<Comment> comments = StreamSupport.stream(iterableComments.spliterator(), false)
                .collect(Collectors.toList());

        ModelMapper modelMapper = new ModelMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd.");

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
        // todo: N+1 issue
        return (int) commentRepository.countByPostId(postId);
    }
    
}
