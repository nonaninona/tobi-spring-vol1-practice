package com.example.chapter6.v2.upgradeLevelPolicy;

import com.example.chapter6.v2.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
