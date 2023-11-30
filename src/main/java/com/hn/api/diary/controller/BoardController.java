package com.hn.api.diary.controller;

import java.net.HttpURLConnection;
import java.util.List;

import com.hn.api.diary.dto.*;
import com.hn.api.diary.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hn.api.diary.response.ListDataResponse;
import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/board/read/{postId}")
    public ResponseEntity<PlainDataResponse<BoardReadDTO>> read(@PathVariable Long postId){
        BoardReadDTO boardReadDTO = boardService.read(postId);

        PlainDataResponse<BoardReadDTO> response = PlainDataResponse.<BoardReadDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(boardReadDTO)
                .build();

        ResponseEntity<PlainDataResponse<BoardReadDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @PutMapping(value = "/board/update")
    public void update(@RequestBody BoardUpdateDTO boardUpdateDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        boardService.update(boardUpdateDTO, userId, postDetailId);
    }

    @PostMapping(value = "/board/reply")
    public void reply(@RequestBody BoardReplyDTO boardReplyDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        boardService.reply(boardReplyDTO, userId, postDetailId);
    }

    @PostMapping(value = "/board/write")
    public void write(@RequestBody BoardWriteDTO boardWriteDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        boardService.write(boardWriteDTO, userId);
    }

    @GetMapping(value = "/board/posts")
    public ResponseEntity<ListDataResponse<BoardPostsDTO>> getPosts(int page, String sort){
        List<BoardPostsDTO> posts = boardService.getPosts(page, sort);

        ListDataResponse<BoardPostsDTO> response = ListDataResponse.<BoardPostsDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(posts)
                .build();

        ResponseEntity<ListDataResponse<BoardPostsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @GetMapping(value = "/board/posts/totalPostsCount")
    public ResponseEntity<PlainDataResponse<Integer>> getTotalPostsCount(){
        int postsCount = boardService.getTotalPostsCount();

        PlainDataResponse<Integer> plainDataResponse = PlainDataResponse.<Integer>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(postsCount)
                .build();

        ResponseEntity<PlainDataResponse<Integer>> responseEntity
                = ResponseEntity.status(HttpStatus.OK).body(plainDataResponse);

        return responseEntity;
    }
}
