package com.hn.api.diary.exception;

public abstract class MyException extends RuntimeException{

    public MyException(String message){
        super(message);
    }

    public abstract int getStatus();
}
