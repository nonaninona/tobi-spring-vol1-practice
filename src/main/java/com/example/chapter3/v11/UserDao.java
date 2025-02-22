package com.example.chapter3.v11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void deleteAll() throws SQLException {
//        jdbcTemplate.update(
//                new PreparedStatementCreator() {
//                    @Override
//                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                        return con.prepareStatement("delete from users");
//                    }
//                }
//        );
        jdbcTemplate.update("delete from users");
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

//    public User get(String id) throws ClassNotFoundException, SQLException {
//        Connection c = dataSource.getConnection();;
//
//        PreparedStatement ps = strategy.makeStatement(c);
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setName(rs.getString("name"));
//        user.setPassword(rs.getString("password"));
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        return user;
//    }


//    public int getCount() throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//
//        try {
//            c = dataSource.getConnection();
//
//            ps = strategy.makeStatement(c);
//
//            rs = ps.executeQuery();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//
//                }
//            }
//
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//
//                }
//            }
//
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//
//                }
//            }
//        }
//    }
}
