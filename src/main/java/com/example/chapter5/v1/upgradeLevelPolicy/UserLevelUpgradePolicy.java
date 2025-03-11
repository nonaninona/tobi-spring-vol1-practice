package com.example.chapter5.v1.upgradeLevelPolicy;

import com.example.chapter5.v1.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
