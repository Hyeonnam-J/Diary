package com.hn.api.diary.controller;

import com.hn.api.diary.exception.MyException;
import com.hn.api.diary.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpURLConnection;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorResponse> diaryExceptionHandler(MyException e){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        int status = e.getStatus();

        ResponseEntity<ErrorResponse> responseEntity
                = ResponseEntity.status(status).body(errorResponse);

        return responseEntity;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpURLConnection.HTTP_BAD_REQUEST)
                .message(e.getBindingResult().getFieldError().getDefaultMessage())
                .build();

        ResponseEntity<ErrorResponse> responseEntity
                = ResponseEntity.status(HttpURLConnection.HTTP_BAD_REQUEST).body(errorResponse);

        return responseEntity;
    }
}
