package com.example.chapter5.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;

    private Level level;
    private int loginCount;
    private int recommendedCount;

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if(nextLevel == null)
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다");
        this.level = this.level.nextLevel();
    }
}
