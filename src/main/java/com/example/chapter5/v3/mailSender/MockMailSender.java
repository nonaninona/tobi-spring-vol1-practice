package com.example.chapter5.v3.mailSender;

import lombok.Getter;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

public class MockMailSender implements MailSender {
    @Getter
    private List<String> requests = new ArrayList<>();

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        requests.add(simpleMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }
}
