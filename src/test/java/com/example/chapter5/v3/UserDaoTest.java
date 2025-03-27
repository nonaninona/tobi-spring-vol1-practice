package com.example.chapter5.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TobiConfig.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setup() {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
//        userDao = ac.getBean(UserDao.class);
//        dataSource = ac.getBean(DataSource.class);

        this.user1 = new User("userA", "유저A", "passwordA", "emailA", Level.BASIC, 1, 0);
        this.user2 = new User("userB", "유저B", "passwordB", "emailB", Level.SILVER, 55, 10);
        this.user3 = new User("userC", "유저C", "passwordC", "emailC", Level.GOLD, 100, 40);

        userDao.deleteAll();
    }

    @Test
    void addAndGet() {
        userDao.add(user1);
        User findUser1 = userDao.get(user1.getId());
        checkSameUser(user1, findUser1);

        userDao.add(user2);
        User findUser2 = userDao.get(user2.getId());
        checkSameUser(user2, findUser2);
    }

    @Test
    void duplicateKey() {
        User user = new User();
        user.setId("ihh0529");
        user.setName("이현희");
        user.setPassword("test");
        user.setLevel(Level.BASIC);
        user.setLoginCount(0);
        user.setRecommendedCount(0);

        userDao.add(user);

        Assertions.assertThrows(DuplicateKeyException.class, () -> userDao.add(user));
    }

    @Test
    void duplicateKeyTranslate() {
        User user = new User();
        user.setId("ihh0529");
        user.setName("이현희");
        user.setPassword("test");
        user.setLevel(Level.BASIC);
        user.setLoginCount(0);
        user.setRecommendedCount(0);

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

    @Test
    void update() {
        //given
        userDao.add(user1);
        userDao.add(user2);

        //when
        user1.setName("userD");
        user1.setPassword("passwordD");
        user1.setLevel(Level.SILVER);
        user1.setLoginCount(0);
        user1.setRecommendedCount(0);
        int updateCount = userDao.update(user1);

        User findUser1 = userDao.get(user1.getId());
        User findUser2 = userDao.get(user2.getId());

        //then
        checkSameUser(user1, findUser1);
        checkSameUser(user2, findUser2);
        assertThat(updateCount).isEqualTo(1);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLoginCount()).isEqualTo(user2.getLoginCount());
        assertThat(user1.getRecommendedCount()).isEqualTo(user2.getRecommendedCount());
    }
}   