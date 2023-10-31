package com.hn.api.diary.exception;

public abstract class DiaryException extends RuntimeException{

    public DiaryException(String message){
        super(message);
    }

    public abstract int getStatus();
}
