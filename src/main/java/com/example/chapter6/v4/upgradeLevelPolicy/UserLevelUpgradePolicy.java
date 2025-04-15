package com.example.chapter6.v4.upgradeLevelPolicy;

import com.example.chapter6.v4.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
