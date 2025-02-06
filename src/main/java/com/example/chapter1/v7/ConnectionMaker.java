package com.example.chapter1.v7;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
    Connection getConnection() throws ClassNotFoundException, SQLException;
}
