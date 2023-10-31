package com.hn.api.diary.controller;

import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(InvalidValue.class)
    public ResponseEntity<ErrorResponse> invalidValue(InvalidValue e){

        int status = e.getStatus();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        ResponseEntity<ErrorResponse> responseEntity
                = ResponseEntity.status(status).body(errorResponse);

        return responseEntity;
    }
}
