package com.example.chapter1.v6;

public class DaoFactory {
    public UserDao getUserDao() {
        return new UserDao(new SimpleConnectionMaker());
    }
}
