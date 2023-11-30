package com.hn.api.diary.controller;

import java.net.HttpURLConnection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.response.PlainDataResponse;

@RestController
public class AuthController {

    @GetMapping(value = "/user")
    public ResponseEntity<PlainDataResponse<String>> user() {
        return success();
    }

    @GetMapping(value = "/admin")
    public ResponseEntity<PlainDataResponse<String>> admin() {
        return success();
    }
    
    private ResponseEntity<PlainDataResponse<String>> success(){
        PlainDataResponse<String> plainDataResponse = PlainDataResponse.<String>builder()
                .status(HttpURLConnection.HTTP_OK)
                .data("OK")
                .build();

        return ResponseEntity.status(plainDataResponse.getStatus()).body(plainDataResponse);
    }
}
