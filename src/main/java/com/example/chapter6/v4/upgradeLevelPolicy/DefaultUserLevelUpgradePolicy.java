package com.example.chapter6.v4.upgradeLevelPolicy;

import com.example.chapter6.v4.User;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMENED_COUNT_FOR_GOLD = 30;

    @Override
    public boolean canUpgradeLevel(User user) {
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

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }
}
