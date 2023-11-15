package com.hn.api.diary.exception;

public class Unauthorization extends DiaryException{

    private static final String MESSAGE = "unauthorization";

    public Unauthorization(){
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 401;
    }
}
