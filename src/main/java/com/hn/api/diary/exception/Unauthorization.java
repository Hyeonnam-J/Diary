package com.hn.api.diary.exception;

public class Unauthorization extends MyException {

    private static final String MESSAGE = "unauthorization";

    public Unauthorization(){
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 401;
    }
}
