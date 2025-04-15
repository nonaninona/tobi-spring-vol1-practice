package com.example.chapter6.v4.config;

import com.example.chapter6.v4.UserDao;
import com.example.chapter6.v4.proxy.TransactionHandler;
import com.example.chapter6.v4.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy;
import com.example.chapter6.v4.upgradeLevelPolicy.UserLevelUpgradePolicy;
import com.example.chapter6.v4.userService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;


@Configuration
@RequiredArgsConstructor
public class UserServiceConfig {
    private final DataSource dataSource;
    private final MailSender mailSender;
    private final UserDao userDao;

    @Bean
    public UserServiceDynamicProxy userServiceDynamicProxy(PlatformTransactionManager platformTransactionManager) {
        UserServiceDynamicProxy userServiceProxy = (UserServiceDynamicProxy) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserServiceDynamicProxy.class},
                new TransactionHandler(userServiceImpl(), platformTransactionManager, "upgradeUserLevels")
        );
        return userServiceProxy;
    }

    @Bean
    public UserServiceDynamicProxyImpl userServiceDynamicProxyImpl() {
        UserServiceDynamicProxyImpl userServiceDynamicProxyImpl = new UserServiceDynamicProxyImpl(userDao, userLevelUpgradePolicy(), mailSender);
        return userServiceDynamicProxyImpl;
    }

    @Bean
    public UserService userService() {
        UserService userService = new UserServiceTx(platformTransactionManager(), userServiceImpl());
        return userService;
    }

    @Bean
    public UserServiceImpl userServiceImpl() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(
                userDao,
                userLevelUpgradePolicy(),
                mailSender
        );
        return userServiceImpl;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        return platformTransactionManager;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        UserLevelUpgradePolicy userLevelUpgradePolicy = new DefaultUserLevelUpgradePolicy();
        return userLevelUpgradePolicy;
    }
}
