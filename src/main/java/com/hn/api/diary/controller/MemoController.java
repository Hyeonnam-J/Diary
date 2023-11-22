package com.hn.api.diary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoController {

    @GetMapping(value = "/memo/{userId}")
    public String getMemoList(@PathVariable Long userId){
        System.out.println(userId);
        return "{\"data\": \"auth is memo\"}";
    }
}
