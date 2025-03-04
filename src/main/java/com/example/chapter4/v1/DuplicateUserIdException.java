package com.example.chapter4.v1;

public class DuplicateUserIdException extends Exception {
    public DuplicateUserIdException(Exception e) {
        super(e);
    }
}
