package com.example.chapter5.v2;

import com.example.chapter5.v2.upgradeLevelPolicy.UserLevelUpgradePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserLevelUpgradePolicy levelUpgradePolicy;
    private final PlatformTransactionManager platformTransactionManager;

    public void upgradeLevels() {
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> allUsers = userDao.getAll();

            for (User user : allUsers) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            platformTransactionManager.commit(status);
        } catch (RuntimeException e) {
            platformTransactionManager.rollback(status);
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        levelUpgradePolicy.upgradeLevel(user);
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        return levelUpgradePolicy.canUpgradeLevel(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
