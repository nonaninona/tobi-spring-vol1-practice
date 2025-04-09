package com.example.chapter6.v3.userService;

import com.example.chapter6.v3.Level;
import com.example.chapter6.v3.User;
import com.example.chapter6.v3.UserDao;
import com.example.chapter6.v3.upgradeLevelPolicy.UserLevelUpgradePolicy;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public class UserServiceDynamicProxyImpl implements UserServiceDynamicProxy {
    private final UserDao userDao;
    private final UserLevelUpgradePolicy levelUpgradePolicy;
    @Setter
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public UserServiceDynamicProxyImpl(UserDao userDao, UserLevelUpgradePolicy levelUpgradePolicy, MailSender mailSender) {
        this.userDao = userDao;
        this.levelUpgradePolicy = levelUpgradePolicy;
        this.mailSender = mailSender;
    }

    public void upgradeLevels() {
        List<User> allUsers = userDao.getAll();

        for (User user : allUsers) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    protected void upgradeLevel(User user) {
        if(user.getId().equals(2)) {
            throw new IllegalArgumentException();
        }
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
