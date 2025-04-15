package com.example.chapter6.v4.factoryBean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {
    private String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
