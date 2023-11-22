package com.hn.api.diary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @GetMapping(value = "/board/posts")
    public String getPostsList(){
        return "{\"data\": \"auth is posts\"}";
    }
}
