package com.example.chapter1.v10;

import java.sql.SQLException;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao1 = context.getBean("userDao", UserDao.class);
        CountingConnectionMaker ccm = context.getBean("countingConnectionMaker", CountingConnectionMaker.class);

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

        System.out.println("count = " + ccm.getCount());
    }
}