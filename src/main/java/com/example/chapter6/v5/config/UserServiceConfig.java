package com.example.chapter6.v5.config;

import com.example.chapter6.v5.UserDao;
import com.example.chapter6.v5.proxy.TransactionAdvice;
import com.example.chapter6.v5.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy;
import com.example.chapter6.v5.upgradeLevelPolicy.UserLevelUpgradePolicy;
import com.example.chapter6.v5.userService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
public class UserServiceConfig {
    private final DataSource dataSource;
    private final MailSender mailSender;
    private final UserDao userDao;


    @Bean
    public TransactionAdvice transactionAdvice() {
        TransactionAdvice transactionAdvice = new TransactionAdvice(platformTransactionManager());
        return transactionAdvice;
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("upgrade*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    }

    @Bean
    public ProxyFactoryBean userService() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(userServiceImpl());
        proxyFactoryBean.addAdvisor(transactionAdvisor());

        return proxyFactoryBean;
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
