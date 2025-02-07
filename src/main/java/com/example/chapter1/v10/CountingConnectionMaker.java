package com.example.chapter1.v10;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    private ConnectionMaker realConnectionMaker;
    private int count = 0;

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        count++;
        return realConnectionMaker.getConnection();
    }

    public int getCount() {
        return count;
    }

    public void setRealConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }
}
