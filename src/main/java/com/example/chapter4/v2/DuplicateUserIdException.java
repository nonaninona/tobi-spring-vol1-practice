package com.example.chapter4.v2;

public class DuplicateUserIdException extends Exception {
    public DuplicateUserIdException(Exception e) {
        super(e);
    }
}
