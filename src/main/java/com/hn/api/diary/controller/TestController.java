package com.hn.api.diary.controller;

import com.hn.api.diary.entity.Comment;
import com.hn.api.diary.entity.Post;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.CommentRepository;
import com.hn.api.diary.repository.PostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;

    @GetMapping(value = "/test")
    public String test(){
        return "end";
    }
}
