package com.hn.api.diary.exception;

public class AlreadyReported extends DiaryException{

    private static final String MESSAGE = "Already reported value";

    public AlreadyReported() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 208;
    }
}
