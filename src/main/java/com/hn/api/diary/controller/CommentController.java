package com.hn.api.diary.controller;

import java.net.HttpURLConnection;
import java.util.List;

import com.hn.api.diary.dto.BoardCommentsDTO;
import com.hn.api.diary.response.ListDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.CommentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/board/comments/{postId}")
    public ResponseEntity<ListDataResponse<BoardCommentsDTO>> getComments(@PathVariable Long postId, int page){
        List<BoardCommentsDTO> comments = commentService.getComments(postId, page);

        ListDataResponse<BoardCommentsDTO> response = ListDataResponse.<BoardCommentsDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(comments)
                .build();

        ResponseEntity<ListDataResponse<BoardCommentsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @GetMapping(value = "/board/comments/totalCommentsCount")
    public ResponseEntity<PlainDataResponse<Integer>> getTotalCommentsCount() {
        int boardCount = commentService.getTotalCommentsCount();

        PlainDataResponse<Integer> plainDataResponse = PlainDataResponse.<Integer>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(boardCount)
                .build();

        ResponseEntity<PlainDataResponse<Integer>> responseEntity 
            = ResponseEntity.status(plainDataResponse.getStatus()).body(plainDataResponse);

        return responseEntity;
    }
}
