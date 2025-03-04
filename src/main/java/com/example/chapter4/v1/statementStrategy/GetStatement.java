package com.example.chapter4.v1.statementStrategy;

import com.example.chapter4.v1.StatementStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetStatement implements StatementStrategy {
    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("select * from users where id = ?");
    }
}
