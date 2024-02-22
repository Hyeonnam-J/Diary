package com.hn.api.diary.controller;

import com.hn.api.diary.dto.freeBoard.*;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.response.ListDataResponse;
import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.FreeBoardPostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FreeBoardPostController {

    private final FreeBoardPostService freeBoardPostService;

    @GetMapping(value = "/freeBoard/post/read/{postId}")
    public ResponseEntity<PlainDataResponse<FreeBoardPostReadDTO>> read(@PathVariable Long postId){
        FreeBoardPostReadDTO freeBoardPostReadDTO = freeBoardPostService.read(postId);

        PlainDataResponse<FreeBoardPostReadDTO> response = PlainDataResponse.<FreeBoardPostReadDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(freeBoardPostReadDTO)
                .build();

        ResponseEntity<PlainDataResponse<FreeBoardPostReadDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @DeleteMapping(value = "/freeBoard/post/delete/{postId}")
    public void delete(@PathVariable String postId, HttpServletRequest request){
        String memberId = String.valueOf(request.getAttribute("memberId"));
        freeBoardPostService.delete(postId, memberId);
    }

    @PutMapping(value = "/freeBoard/post/update")
    public void update(@RequestBody @Valid FreeBoardPostUpdateDTO freeBoardPostUpdateDTO, HttpServletRequest request){
        String memberId = String.valueOf(request.getAttribute("memberId"));
        freeBoardPostService.update(freeBoardPostUpdateDTO, memberId);
    }

    @PostMapping(value = "/freeBoard/post/reply")
    public void reply(@RequestBody @Valid FreeBoardPostReplyDTO freeBoardPostReplyDTO, HttpServletRequest request){
        String memberId = String.valueOf(request.getAttribute("memberId"));
        freeBoardPostService.reply(freeBoardPostReplyDTO, memberId);
    }

    @PostMapping(value = "/freeBoard/post/write")
    public void write(@RequestBody @Valid FreeBoardPostWriteDTO freeBoardPostWriteDTO, HttpServletRequest request){
        String memberId = String.valueOf(request.getAttribute("memberId"));
        freeBoardPostService.write(freeBoardPostWriteDTO, memberId);
    }

    @GetMapping(value = "/freeBoard/posts")
    public ResponseEntity<ListDataResponse<FreeBoardPostsDTO>> getPosts(int page, String sort){
        List<FreeBoardPostsDTO> posts = freeBoardPostService.getPosts(page, sort);

        ListDataResponse<FreeBoardPostsDTO> response = ListDataResponse.<FreeBoardPostsDTO>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(posts)
                .build();

        ResponseEntity<ListDataResponse<FreeBoardPostsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }

    @GetMapping(value = "/freeBoard/posts/totalCount")
    public ResponseEntity<PlainDataResponse<Integer>> getTotalCount(){
        int postsCount = freeBoardPostService.getTotalCount();

        PlainDataResponse<Integer> plainDataResponse = PlainDataResponse.<Integer>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data(postsCount)
                .build();

        ResponseEntity<PlainDataResponse<Integer>> responseEntity
                = ResponseEntity.status(HttpStatus.OK).body(plainDataResponse);

        return responseEntity;
    }
}
