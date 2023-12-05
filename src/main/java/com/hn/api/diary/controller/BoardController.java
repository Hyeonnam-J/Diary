package com.hn.api.diary.controller;

import com.hn.api.diary.dto.*;
import com.hn.api.diary.response.ListDataResponse;
import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping(value = "/board/post/read/{postId}")
    public ResponseEntity<PlainDataResponse<BoardPostReadDTO>> boardPostRead(@PathVariable Long postId){
        BoardPostReadDTO boardPostReadDTO = postService.boardPostRead(postId);

        PlainDataResponse<BoardPostReadDTO> response = PlainDataResponse.<BoardPostReadDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(boardPostReadDTO)
                .build();

        ResponseEntity<PlainDataResponse<BoardPostReadDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @DeleteMapping(value = "/board/post/delete")
    public void boardPostDelete(HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        postService.boardPostDelete(userId, postDetailId);
    }

    @PutMapping(value = "/board/post/update")
    public void boardPostUpdate(@RequestBody BoardPostUpdateDTO boardPostUpdateDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        postService.boardPostUpdate(boardPostUpdateDTO, userId, postDetailId);
    }

    @PostMapping(value = "/board/post/reply")
    public void boardPostReply(@RequestBody BoardPostReplyDTO boardPostReplyDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        String postDetailId = String.valueOf(request.getAttribute("postDetailId"));
        postService.boardPostReply(boardPostReplyDTO, userId, postDetailId);
    }

    @PostMapping(value = "/board/post/write")
    public void boardPostWrite(@RequestBody BoardPostWriteDTO boardPostWriteDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        postService.boardPostWrite(boardPostWriteDTO, userId);
    }

    @GetMapping(value = "/board/posts")
    public ResponseEntity<ListDataResponse<BoardPostsDTO>> getBoardPosts(int page, String sort){
        List<BoardPostsDTO> posts = postService.getBoardPosts(page, sort);

        ListDataResponse<BoardPostsDTO> response = ListDataResponse.<BoardPostsDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(posts)
                .build();

        ResponseEntity<ListDataResponse<BoardPostsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @GetMapping(value = "/board/posts/totalPostsCount")
    public ResponseEntity<PlainDataResponse<Integer>> getTotalBoardPostsCount(){
        int postsCount = postService.getTotalBoardPostsCount();
        
        PlainDataResponse<Integer> plainDataResponse = PlainDataResponse.<Integer>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(postsCount)
                .build();

        ResponseEntity<PlainDataResponse<Integer>> responseEntity
                = ResponseEntity.status(HttpStatus.OK).body(plainDataResponse);

        return responseEntity;
    }
}
