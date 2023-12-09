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

    @DeleteMapping(value = "/freeBoard/comment/delete/{commentId}")
    public void delete(@PathVariable String commentId){
        freeBoardCommentService.delete(commentId);
    }

    @PostMapping(value = "/freeBoard/comment/update")
    public void update(@RequestBody FreeBoardCommentUpdateDTO freeBoardCommentUpdateDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardCommentService.update(freeBoardCommentUpdateDTO, userId);
    }

    @PostMapping(value = "/freeBoard/comment/write")
    public void write(@RequestBody FreeBoardCommentWriteDTO freeBoardCommentWriteDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardCommentService.write(freeBoardCommentWriteDTO, userId);
    }

    @PostMapping(value = "/freeBoard/comment/reply")
    public void reply(@RequestBody FreeBoardBoardCommentReplyDTO boardCommentReplyDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardCommentService.reply(boardCommentReplyDTO, userId);
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

    @GetMapping(value = "/freeBoard/comments/totalCount/{postId}")
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
