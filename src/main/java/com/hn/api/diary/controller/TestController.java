package com.hn.api.diary.controller;

import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.ParameterTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @Autowired private UserRepository userRepository;
//    @Autowired private FreeBoardPostRepository freeBoardPostRepository;
//    @Autowired private FreeBoardCommentRepository freeBoardCommentRepository;

    static final Logger logger = LoggerFactory.getLogger(TestController.class);

    static String test = "test 11";

    @GetMapping(value = "/test")
    public String test(){
        new Test().test();
        logger.info("jhn test controller class ...");
        System.out.println(test);
        return test;
    }
}
