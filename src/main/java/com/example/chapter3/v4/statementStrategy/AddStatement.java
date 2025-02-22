package com.example.chapter3.v4.statementStrategy;

import com.example.chapter3.v4.StatementStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {
    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
    }
}
