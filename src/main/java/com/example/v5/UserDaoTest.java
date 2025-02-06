package com.example.v5;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        UserDao userDao = new NUserDao();
//        UserDao userDao = new DUserDao();
        UserDao userDao1 = new UserDao(new SimpleConnectionMaker());
        UserDao userDao2 = new UserDao(new NConnectionMaker());
        UserDao userDao3 = new UserDao(new DConnectionMaker());

        User user = new User();
        user.setId("ihh0529");
        user.setName("이현희");
        user.setPassword("test");

        userDao1.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao1.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}