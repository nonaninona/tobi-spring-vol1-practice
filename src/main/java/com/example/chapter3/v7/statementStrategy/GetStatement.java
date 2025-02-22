package com.example.chapter3.v7.statementStrategy;

import com.example.chapter3.v7.StatementStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetStatement implements StatementStrategy {
    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("select * from users where id = ?");
    }
}
