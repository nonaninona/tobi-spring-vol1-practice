package com.example.chapter3.v3.userDao;

import com.example.chapter3.v3.UserDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDeleteAll extends UserDao {
    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("delete from users");
    }
}
