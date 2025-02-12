package com.example.chapter2.v3.userDao;

import com.example.chapter2.v3.UserDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class userDaoGetCount extends UserDao {
    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("select count(*) from users");
    }
}
