package com.hn.api.diary.exception;

public class Forbidden extends MyException{

    private static final String MESSAGE = "Forbidden";

    public Forbidden() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 403;
    }
}
