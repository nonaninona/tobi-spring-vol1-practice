package com.example.chapter5.v3;

import com.example.chapter5.v3.upgradeLevelPolicy.UserLevelUpgradePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.chapter5.v3.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.example.chapter5.v3.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.MIN_RECOMMENED_COUNT_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = com.example.chapter5.v3.DaoFactory.class)
public class UserServiceTest {

    @Autowired
    private com.example.chapter5.v3.UserService userService;
    @Autowired
    private com.example.chapter5.v3.UserDao userDao;
    @Autowired
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private List<com.example.chapter5.v3.User> users = new ArrayList<>();

    @BeforeEach
    void setup() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(com.example.chapter5.v3.DaoFactory.class);
        userService = ac.getBean("userService", com.example.chapter5.v3.UserService.class);
        userDao = ac.getBean("userDao", com.example.chapter5.v3.UserDao.class);
        userDao.deleteAll();

        users.add(new com.example.chapter5.v3.User("userA", "유저A", "passwordA", com.example.chapter5.v3.Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0));
        users.add(new com.example.chapter5.v3.User("userB", "유저B", "passwordB", com.example.chapter5.v3.Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0));
        users.add(new com.example.chapter5.v3.User("userC", "유저C", "passwordC", com.example.chapter5.v3.Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD-1));
        users.add(new com.example.chapter5.v3.User("userD", "유저D", "passwordD", com.example.chapter5.v3.Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD));
        users.add(new com.example.chapter5.v3.User("userE", "유저E", "passwordE", com.example.chapter5.v3.Level.GOLD,  100, 100));
    }

    @Test
    @DisplayName("유저 업그레이드 테스트")
    void testUserLevelUpgrade() throws Exception {
        // given
        users.forEach(user -> userDao.add(user));

        // when
        userService.upgradeLevels();

        // then
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    @DisplayName("사용자 가입 시 BASIC 레벨 부여")
    void testInitialBasic() {
        // given
        com.example.chapter5.v3.User notInitialUser = users.get(4);
        com.example.chapter5.v3.User initialUser = users.get(0);
        initialUser.setLevel(null);

        // when
        userService.add(notInitialUser);
        userService.add(initialUser);

        com.example.chapter5.v3.User findNotInitialUser = userDao.get(notInitialUser.getId());
        com.example.chapter5.v3.User findInitialUser = userDao.get(initialUser.getId());

        // then
        checkLevel(findNotInitialUser, notInitialUser.getLevel());
        checkLevel(findInitialUser, com.example.chapter5.v3.Level.BASIC);
    }

    private void checkLevel(com.example.chapter5.v3.User user, com.example.chapter5.v3.Level level) {
        com.example.chapter5.v3.User findUser = userDao.get(user.getId());
        assertThat(findUser.getLevel()).isEqualByComparingTo(level);
    }

    private void checkLevelUpgraded(com.example.chapter5.v3.User user, boolean isUpgrad) {
        com.example.chapter5.v3.User findUser = userDao.get(user.getId());
        if(isUpgrad) {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel());
        }
    }



    @Test
    @DisplayName("강제 예외 발생")
    void testNetworkFail() {
        // given
        users.forEach(user -> userDao.add(user));
        com.example.chapter5.v3.UserService testUserService = new TestUserService(userDao, userLevelUpgradePolicy, platformTransactionManager, users.get(3).getId());

        // when

        // then
        assertThrows(TestUserServiceException.class, () -> testUserService.upgradeLevels());
        assertThat(users.get(1).getLevel()).isEqualTo(com.example.chapter5.v3.Level.BASIC);
    }



    private static class TestUserService extends UserService {

        private String id;

        public TestUserService(com.example.chapter5.v3.UserDao userDao, UserLevelUpgradePolicy levelUpgradePolicy, PlatformTransactionManager platformTransactionManager, String id) {
            super(userDao, levelUpgradePolicy, platformTransactionManager);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(com.example.chapter5.v3.User user) {
            if(user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

    }

    private static class TestUserServiceException extends RuntimeException {
        public TestUserServiceException() {
        }
    }
}
