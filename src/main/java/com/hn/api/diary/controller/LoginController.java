package com.hn.api.diary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @ResponseBody
    @GetMapping(value = "/hello")
    public String test(){
        System.out.println("here is server");
        return "가져오나";
    }
}
