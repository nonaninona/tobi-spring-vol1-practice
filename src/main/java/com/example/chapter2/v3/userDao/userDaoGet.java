package com.example.chapter2.v3.userDao;

import com.example.chapter2.v3.UserDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class userDaoGet extends UserDao {
    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("select * from users where id = ?");
    }
}
