package com.example.chapter6.v5.config;

import com.example.chapter6.v5.mailSender.DummyMailSender;
import com.example.chapter6.v5.mailSender.EmailProperties;
import com.example.chapter6.v5.mailSender.MyMailSenderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;

@Configuration
@PropertySource({"classpath:chapter5/application.properties"})
public class MailSenderConfig {
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
}
