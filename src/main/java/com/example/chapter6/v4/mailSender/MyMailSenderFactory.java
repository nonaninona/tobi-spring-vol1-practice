package com.example.chapter6.v4.mailSender;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@RequiredArgsConstructor
public class MyMailSenderFactory {
    private final EmailProperties emailProperties;

    public MailSender createMyMailSender() {
        JavaMailSenderImpl myMailSender = new JavaMailSenderImpl();

        // 필수: SMTP 서버 설정
        myMailSender.setHost(emailProperties.getHost());  // 예: Gmail SMTP
        myMailSender.setPort(emailProperties.getPort());
        myMailSender.setUsername(emailProperties.getUsername()); // 발신자 이메일
        myMailSender.setPassword(emailProperties.getPassword());    // 앱 비밀번호 (Gmail 경우)

        // 추가: TLS/SSL 설정
        Properties props = myMailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", emailProperties.getAuth());
        props.put("mail.smtp.starttls.enable", emailProperties.getStartTls()); // TLS 사용

        return myMailSender;
    }
}
