package com.example.chapter1.v7;

public class DaoFactory {
    public UserDao getUserDao() {
        return new UserDao(getConnectionMaker());
    }
    public UserDao getUserDao1() {
        return new UserDao(getConnectionMaker());
    }
    public UserDao getUserDao2() {
        return new UserDao(getConnectionMaker());
    }

    private ConnectionMaker getConnectionMaker() {
        return new SimpleConnectionMaker();
    }
}
