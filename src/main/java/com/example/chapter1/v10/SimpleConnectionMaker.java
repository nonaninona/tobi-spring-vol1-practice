package com.example.chapter1.v10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker implements ConnectionMaker {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/tobi", "root", "admin");
        return c;
    }
}
