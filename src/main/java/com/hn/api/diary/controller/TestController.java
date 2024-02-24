package com.hn.api.diary.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @Autowired private UserRepository userRepository;
//    @Autowired private FreeBoardPostRepository freeBoardPostRepository;
//    @Autowired private FreeBoardCommentRepository freeBoardCommentRepository;

    static final Logger logger = LoggerFactory.getLogger(TestController.class);

    static String test = "test-18";

    @GetMapping(value = "/test")
    public String test(){
        new Test().test();
        logger.info("jhn test controller class ...");
        System.out.println(test);
        return test;
    }
}
