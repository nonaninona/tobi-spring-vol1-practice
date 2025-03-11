package com.example.chapter5.v1;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMENED_COUNT_FOR_GOLD = 30;

    private final UserDao userDao;

    public void upgradeLevels() {
        List<User> allUsers = userDao.getAll();

        for (User user : allUsers) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        switch (user.getLevel()) {
            case BASIC:
                return (user.getLoginCount() >= MIN_LOGIN_COUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecommendedCount() >= MIN_RECOMMENED_COUNT_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level : " + user.getLevel());
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
