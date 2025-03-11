package com.example.chapter5.v1;

import com.example.chapter5.v1.UserDao;
import com.example.chapter5.v1.UserDaoJdbc;
import com.example.chapter5.v1.UserService;
import com.example.chapter5.v1.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy;
import com.example.chapter5.v1.upgradeLevelPolicy.UserLevelUpgradePolicy;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class TestConfig {

    @Bean
    public UserService userService() {
        UserService userService = new UserService(userDao(), userLevelUpgradePolicy());
        return userService;
    }

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

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        UserLevelUpgradePolicy userLevelUpgradePolicy = new DefaultUserLevelUpgradePolicy();
        return userLevelUpgradePolicy;
    }
}
