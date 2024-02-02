package com.hn.api.diary.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.dto.freeBoard.FreeBoardPostReadDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostReplyDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostUpdateDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostWriteDTO;
import com.hn.api.diary.dto.freeBoard.FreeBoardPostsDTO;
import com.hn.api.diary.response.ListDataResponse;
import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.FreeBoardPostService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

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
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardPostService.delete(postId, userId);
    }

    @PutMapping(value = "/freeBoard/post/update")
    public void update(@RequestBody FreeBoardPostUpdateDTO freeBoardPostUpdateDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardPostService.update(freeBoardPostUpdateDTO, userId);
    }

    @PostMapping(value = "/freeBoard/post/reply")
    public void reply(@RequestBody FreeBoardPostReplyDTO freeBoardPostReplyDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardPostService.reply(freeBoardPostReplyDTO, userId);
    }

    @PostMapping(value = "/freeBoard/post/write")
    public void write(@RequestBody FreeBoardPostWriteDTO freeBoardPostWriteDTO, HttpServletRequest request){
        String userId = String.valueOf(request.getAttribute("userId"));
        freeBoardPostService.write(freeBoardPostWriteDTO, userId);
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
