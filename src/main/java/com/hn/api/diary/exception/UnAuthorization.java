package com.hn.api.diary.exception;

public class UnAuthorization extends DiaryException{

    private static final String MESSAGE = "un authorization";

    public UnAuthorization(){
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 401;
    }
}
