package com.example.chapter6.v5.userService;

import com.example.chapter6.v5.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RequiredArgsConstructor
public class UserServiceTx implements UserService {

    private final PlatformTransactionManager platformTransactionManager;
    private final UserServiceImpl userService;

    @Override
    public void upgradeLevels() {
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();
            platformTransactionManager.commit(status);
        } catch (RuntimeException e) {
            platformTransactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }

}
