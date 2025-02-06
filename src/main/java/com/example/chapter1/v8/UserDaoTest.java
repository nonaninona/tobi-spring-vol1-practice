package com.example.chapter1.v8;

import java.sql.SQLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DaoFactory daoFactory = new DaoFactory();
        UserDao dao1 = daoFactory.userDao();
        System.out.println("dao1 = " + dao1);
        UserDao dao2 = daoFactory.userDao();
        System.out.println("dao2 = " + dao2);

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        System.out.println("userDao = " + userDao);
        UserDao userDao2 = context.getBean("userDao", UserDao.class);
        System.out.println("userDao2 = " + userDao2);

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