package com.example.chapter6.v4.factoryBean;

import com.example.chapter6.v4.proxy.TransactionHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

@AllArgsConstructor
public class TxProxyFactoryBean implements FactoryBean<Object> {

    private Object target;
    private PlatformTransactionManager txManager;
    private String pattern;

    private Class<?> serviceInterface;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { serviceInterface },
                new TransactionHandler(target, txManager, pattern)
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }
}
