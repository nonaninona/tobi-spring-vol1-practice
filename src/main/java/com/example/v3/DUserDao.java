package com.example.v3;

import java.sql.Connection;
import java.sql.SQLException;

public class DUserDao extends UserDao {

    @Override
    Connection getConnection() throws ClassNotFoundException, SQLException {
        // DB 생성 코드
        return null;
    }
}
