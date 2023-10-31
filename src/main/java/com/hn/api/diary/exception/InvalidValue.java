package com.hn.api.diary.exception;

public class InvalidValue extends RuntimeException{

    private static final String MESSAGE = "invalid value";

    public int getStatus(){
        return 404;
    }

    public InvalidValue() {
        super(MESSAGE);
    }

}
