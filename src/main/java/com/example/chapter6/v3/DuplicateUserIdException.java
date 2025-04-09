package com.example.chapter6.v3;

public class DuplicateUserIdException extends Exception {
    public DuplicateUserIdException(Exception e) {
        super(e);
    }
}
