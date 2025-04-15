package com.example.chapter6.v5;

public class DuplicateUserIdException extends Exception {
    public DuplicateUserIdException(Exception e) {
        super(e);
    }
}
