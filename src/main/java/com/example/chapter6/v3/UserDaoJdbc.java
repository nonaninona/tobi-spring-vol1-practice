package com.example.chapter6.v3;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    @Override
    public void add(User user) {
//        try {
        jdbcTemplate.update(
                "insert into users(id, name, password, email, level, login_count, recommended_count) values (?, ?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLoginCount(),
                user.getRecommendedCount());
//        } catch (DuplicateKeyException e) {
//            throw new DuplicateUserIdException(e);
//        }
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update(
                "update users set name = ?, password = ?, email = ?, level = ?, login_count = ?, recommended_count = ? where id = ?",
                user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLoginCount(),
                user.getRecommendedCount(), user.getId());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(
                "select id, name, password, email, level, login_count, recommended_count from users where id = ?",
                new Object[]{id},
                getRowMapper());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users",
                getRowMapper()
        );
    }

    private static RowMapper<User> getRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLoginCount(rs.getInt("login_count"));
                user.setRecommendedCount(rs.getInt("recommended_count"));
                return user;
            }
        };
    }
}
