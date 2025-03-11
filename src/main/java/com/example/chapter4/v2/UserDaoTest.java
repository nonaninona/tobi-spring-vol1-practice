package com.example.chapter4.v2;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class UserDaoTest {
    private UserDao userDao;
    private DataSource dataSource;

    @BeforeEach
    void setup() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = ac.getBean(UserDao.class);
        dataSource = ac.getBean(DataSource.class);
    }


    @Test
    void duplicateKey() {
        userDao.deleteAll();

        User user = new User();
        user.setId("ihh0529");
        user.setName("이현희");
        user.setPassword("test");

        userDao.add(user);

        Assertions.assertThrows(DuplicateKeyException.class, () -> userDao.add(user));
    }

    @Test
    void duplicateKeyTranslate() {
        userDao.deleteAll();

        User user = new User();
        user.setId("ihh0529");
        user.setName("이현희");
        user.setPassword("test");

        try {
            userDao.add(user);
            userDao.add(user);
        } catch (DuplicateKeyException e) {
            SQLException sqlException = (SQLException) e.getCause();
            SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            assertThat(translator.translate(null, null, sqlException))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }
}   