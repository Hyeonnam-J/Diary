package com.hn.api.diary.controller;

import java.net.HttpURLConnection;
import java.util.List;

import com.hn.api.diary.dto.FreeBoardBoardCommentReplyDTO;
import com.hn.api.diary.dto.FreeBoardCommentUpdateDTO;
import com.hn.api.diary.dto.FreeBoardCommentWriteDTO;
import com.hn.api.diary.dto.FreeBoardCommentsDTO;
import com.hn.api.diary.response.ListDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.FreeBoardCommentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FreeBoardCommentController {

    private final FreeBoardCommentService freeBoardCommentService;

    @DeleteMapping(value = "/freeBoard/comment/delete")
    public void delete(HttpServletRequest request){
        String commentId = String.valueOf(request.getAttribute("commentId"));
        freeBoardCommentService.delete(commentId);
    }

    @PostMapping(value = "/freeBoard/comment/update")
    public void update(@RequestBody FreeBoardCommentUpdateDTO freeBoardCommentUpdateDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        String commentId = String.valueOf(request.getAttribute("commentId"));

        freeBoardCommentService.update(freeBoardCommentUpdateDTO, userId, postDetailId, commentId);
    }

    @PostMapping(value = "/freeBoard/comment/write")
    public void write(@RequestBody FreeBoardCommentWriteDTO freeBoardCommentWriteDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        freeBoardCommentService.write(freeBoardCommentWriteDTO, userId, postDetailId);
    }

    @PostMapping(value = "/freeBoard/comment/reply")
    public void reply(@RequestBody FreeBoardBoardCommentReplyDTO boardCommentReplyDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        String commentId = String.valueOf(request.getAttribute("commentId"));
        freeBoardCommentService.reply(boardCommentReplyDTO, userId, postDetailId, commentId);
    }

    @GetMapping(value = "/freeBoard/comments/{postId}")
    public ResponseEntity<ListDataResponse<FreeBoardCommentsDTO>> getComments(@PathVariable Long postId, int page){
        List<FreeBoardCommentsDTO> comments = freeBoardCommentService.getComments(postId, page);

        ListDataResponse<FreeBoardCommentsDTO> response = ListDataResponse.<FreeBoardCommentsDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(comments)
                .build();

        ResponseEntity<ListDataResponse<FreeBoardCommentsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @GetMapping(value = "/freeBoard/comments/totalCommentsCount/{postId}")
    public ResponseEntity<PlainDataResponse<Integer>> getTotalCount(@PathVariable Long postId) {
        int commentsCount = freeBoardCommentService.getTotalCount(postId);

        PlainDataResponse<Integer> plainDataResponse = PlainDataResponse.<Integer>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(commentsCount)
                .build();

        ResponseEntity<PlainDataResponse<Integer>> responseEntity 
            = ResponseEntity.status(plainDataResponse.getStatus()).body(plainDataResponse);

        return responseEntity;
    }
}
