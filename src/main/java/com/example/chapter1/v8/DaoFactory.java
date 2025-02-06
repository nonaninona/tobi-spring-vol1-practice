package com.example.chapter1.v8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(getConnectionMaker());
    }
    public UserDao getUserDao1() {
        return new UserDao(getConnectionMaker());
    }
    public UserDao getUserDao2() {
        return new UserDao(getConnectionMaker());
    }

    private ConnectionMaker getConnectionMaker() {
        return new SimpleConnectionMaker();
    }
}
