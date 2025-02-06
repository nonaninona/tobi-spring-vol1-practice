package com.example.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/tobi", "root", "admin");
        return c;
    }
}
