package com.example.chapter6.v5.config;

import com.example.chapter6.v5.UserDao;
import com.example.chapter6.v5.UserDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc(dataSource());
        return userDaoJdbc;
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/tobi");
        dataSource.setUsername("root");
        dataSource.setPassword("admin");

        return dataSource;
    }
}
