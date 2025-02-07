package com.example.chapter1.v9;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    private ConnectionMaker realConnectionMaker;
    private int count = 0;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        count++;
        return realConnectionMaker.getConnection();
    }

    public int getCount() {
        return count;
    }
}
