package com.example.chapter1.v3;

import java.sql.Connection;
import java.sql.SQLException;

public class NUserDao extends UserDao {
    @Override
    Connection getConnection() throws ClassNotFoundException, SQLException {
        // DB 생성 코드
        return null;
    }
}
