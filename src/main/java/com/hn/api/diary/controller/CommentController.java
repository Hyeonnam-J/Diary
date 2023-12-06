package com.hn.api.diary.controller;

import java.net.HttpURLConnection;
import java.util.List;

import com.hn.api.diary.dto.BoardCommentReplyDTO;
import com.hn.api.diary.dto.BoardCommentWriteDTO;
import com.hn.api.diary.dto.BoardCommentsDTO;
import com.hn.api.diary.response.ListDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.CommentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/board/comment/write")
    public void boardCommentWrite(@RequestBody BoardCommentWriteDTO boardCommentWriteDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        commentService.boardCommentWirte(boardCommentWriteDTO, userId, postDetailId);
    }

    @PostMapping(value = "/board/comment/reply")
    public void boardCommentReply(@RequestBody BoardCommentReplyDTO boardCommentReplyDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        String commentId = String.valueOf(request.getAttribute("commentId"));
        commentService.boardCommentReply(boardCommentReplyDTO, userId, postDetailId, commentId);
    }

    @GetMapping(value = "/board/comments/{postId}")
    public ResponseEntity<ListDataResponse<BoardCommentsDTO>> getBoardComments(@PathVariable Long postId, int page){
        List<BoardCommentsDTO> comments = commentService.getBoardComments(postId, page);

        ListDataResponse<BoardCommentsDTO> response = ListDataResponse.<BoardCommentsDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(comments)
                .build();

        ResponseEntity<ListDataResponse<BoardCommentsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @GetMapping(value = "/board/comments/totalCommentsCount/{postId}")
    public ResponseEntity<PlainDataResponse<Integer>> getTotalCommentsCount(@PathVariable Long postId) {
        int commentsCount = commentService.getTotalCommentsCount(postId);

        PlainDataResponse<Integer> plainDataResponse = PlainDataResponse.<Integer>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(commentsCount)
                .build();

        ResponseEntity<PlainDataResponse<Integer>> responseEntity 
            = ResponseEntity.status(plainDataResponse.getStatus()).body(plainDataResponse);

        return responseEntity;
    }
}
