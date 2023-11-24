package com.hn.api.diary.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.BoardWriteDTO;
import com.hn.api.diary.response.ListDataResponse;
import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/board/write")
    public void post(@RequestBody BoardWriteDTO boardWriteDTO, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        boardService.post(boardWriteDTO, userId);

        System.out.println("post");
    }

    @GetMapping(value = "/board/posts")
    public ResponseEntity<ListDataResponse<BoardPostsDTO>> getPosts(Pageable pageable){
        List<BoardPostsDTO> posts = boardService.getPosts(pageable);

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
