package com.example.chapter5.v3.mailSender;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
    }
}
