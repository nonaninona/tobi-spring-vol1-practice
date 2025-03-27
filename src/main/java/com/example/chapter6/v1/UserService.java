package com.example.chapter6.v1;

import com.example.chapter6.v1.upgradeLevelPolicy.UserLevelUpgradePolicy;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserService {
    private final UserDao userDao;
    private final UserLevelUpgradePolicy levelUpgradePolicy;
    private final PlatformTransactionManager platformTransactionManager;
    @Setter
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public UserService(UserDao userDao, UserLevelUpgradePolicy levelUpgradePolicy, PlatformTransactionManager platformTransactionManager, MailSender mailSender) {
        this.userDao = userDao;
        this.levelUpgradePolicy = levelUpgradePolicy;
        this.platformTransactionManager = platformTransactionManager;
        this.mailSender = mailSender;
    }

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
        sendUpgradeMail(user);
    }

    private void sendUpgradeMail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(username);
        message.setSubject("Upgrade 안내(test)");
        message.setText("사용자님의 등급이 " + user.getLevel().toString() + "로 업그레이드 되었습니다!");

        mailSender.send(message);
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
