package com.example.chapter6.v5.proxy;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RequiredArgsConstructor
public class TransactionAdvice implements MethodInterceptor {

    private final PlatformTransactionManager txManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Object ret = invocation.proceed();
            txManager.commit(status);
            return ret;
        } catch (Throwable e) {
            txManager.rollback(status);
            throw e;
        }
    }
}
