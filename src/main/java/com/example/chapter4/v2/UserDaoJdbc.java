package com.example.chapter4.v2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    public void add(User user) {
//        try {
            jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",
                    user.getId(), user.getName(), user.getPassword());
//        } catch (DuplicateKeyException e) {
//            throw new DuplicateUserIdException(e);
//        }
    }

    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject("select id, name, password where id = ?",
                new Object[] {id},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));
                        return user;
                    }
                });
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }
}
