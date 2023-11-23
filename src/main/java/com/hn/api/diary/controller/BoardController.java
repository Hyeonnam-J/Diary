package com.hn.api.diary.controller;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.WriteBoardDTO;
import com.hn.api.diary.response.DataResponse;
import com.hn.api.diary.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<DataResponse<BoardPostsDTO>> getPosts(Pageable pageable){
        List<BoardPostsDTO> posts = boardService.getPosts(pageable);

        DataResponse<BoardPostsDTO> response = DataResponse.<BoardPostsDTO>builder()
                .data(posts)
                .build();

        ResponseEntity<DataResponse<BoardPostsDTO>> responseEntity
                = ResponseEntity.status(response.getStatus()).body(response);

        return responseEntity;
    }
}
