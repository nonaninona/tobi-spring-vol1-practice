package com.example.chapter6.v4.factoryBean;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

@Setter
public class MessageFactoryBean implements FactoryBean<Message> {
    private String text;

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }
}
