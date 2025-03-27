package com.example.chapter6.v1;

import com.example.chapter6.v1.mailSender.DummyMailSender;
import com.example.chapter6.v1.mailSender.EmailProperties;
import com.example.chapter6.v1.mailSender.MyMailSenderFactory;
import com.example.chapter6.v1.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy;
import com.example.chapter6.v1.upgradeLevelPolicy.UserLevelUpgradePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@PropertySource({"classpath:chapter5/application.properties"})
public class TobiConfig {

    @Bean
    public UserService userService() {
        UserService userService = new UserService(
                userDao(),
                userLevelUpgradePolicy(),
                platformTransactionManager(),
                mailSender()
        );
        return userService;
    }

    @Bean
    public MailSender mailSender() {
//        MailSender mailSender = myMailSenderFactory().createMyMailSender();
        DummyMailSender mailSender = new DummyMailSender();
        return mailSender;
    }

    @Bean
    public MyMailSenderFactory myMailSenderFactory() {
        MyMailSenderFactory myMailSenderFactory = new MyMailSenderFactory(emailProperties());
        return myMailSenderFactory;
    }

    @Bean
    public EmailProperties emailProperties() {
        EmailProperties emailProperties = new EmailProperties();
        return emailProperties;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource());
        return platformTransactionManager;
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
