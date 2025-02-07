package com.example.chapter1.v9;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(countingConnectionMaker());
    }

    @Bean
    public ConnectionMaker countingConnectionMaker() {
        return new CountingConnectionMaker(simpleConnectionMaker());
    }

    @Bean
    public ConnectionMaker simpleConnectionMaker() { return new SimpleConnectionMaker(); }
}
