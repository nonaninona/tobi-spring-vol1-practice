package com.example.chapter1.v6;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DaoFactory daoFactory = new DaoFactory();

        UserDao userDao = daoFactory.getUserDao();

        User user = new User();
        user.setId("ihh0529");
        user.setName("이현희");
        user.setPassword("test");

        userDao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}