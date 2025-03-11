package com.example.chapter5.v1;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public void upgradeLevels() {
        List<User> allUsers = userDao.getAll();

        for (User user : allUsers) {
            boolean isChange = false;


            if(user.getLevel() == Level.BASIC && user.getLoginCount() >= 50) {
                user.setLevel(Level.SILVER);
                isChange = true;
            } else if(user.getLevel() == Level.SILVER && user.getRecommendedCount() >= 30) {
                user.setLevel(Level.GOLD);
                isChange = true;
            }

            if(isChange) {
                userDao.update(user);
            }
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
