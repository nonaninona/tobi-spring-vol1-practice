package com.example.chapter6.v5.upgradeLevelPolicy;

import com.example.chapter6.v5.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
