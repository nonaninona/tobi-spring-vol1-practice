package com.example.chapter5.v2;

import com.example.chapter5.v2.upgradeLevelPolicy.UserLevelUpgradePolicy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserLevelUpgradePolicy levelUpgradePolicy;

    public void upgradeLevels() {
        List<User> allUsers = userDao.getAll();

        for (User user : allUsers) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
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
