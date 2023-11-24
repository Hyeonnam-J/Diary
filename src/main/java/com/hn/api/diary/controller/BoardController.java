package com.hn.api.diary.controller;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.WriteBoardDTO;
import com.hn.api.diary.response.ListDataResponse;
import com.hn.api.diary.response.PlainDataResponse;
import com.hn.api.diary.service.BoardService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.net.HttpURLConnection;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/board/post")
    public void post(@RequestBody WriteBoardDTO writeBoardDTO, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        boardService.post(writeBoardDTO, userId);

        System.out.println("post");
    }

    @GetMapping(value = "/board/posts")
    public ResponseEntity<ListDataResponse<BoardPostsDTO>> getPosts(Pageable pageable){
        System.out.println("여기: "+pageable.getPageNumber());
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
