package com.example.chapter5.v3.upgradeLevelPolicy;

import com.example.chapter5.v3.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
