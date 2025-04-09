package com.example.chapter6.v3.upgradeLevelPolicy;

import com.example.chapter6.v3.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
