package com.example.chapter5.v3.mailSender;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class EmailProperties {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String timeout;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String startTls;
}
