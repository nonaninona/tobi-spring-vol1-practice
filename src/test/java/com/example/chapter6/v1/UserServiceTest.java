package com.example.chapter6.v1;

import com.example.chapter6.v1.mailSender.MockMailSender;
import com.example.chapter6.v1.upgradeLevelPolicy.UserLevelUpgradePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.chapter6.v1.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.example.chapter6.v1.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.MIN_RECOMMENED_COUNT_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = TobiConfig.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private List<User> users = new ArrayList<>();
    @Autowired
    private MailSender mailSender;

    @BeforeEach
    void setup() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TobiConfig.class);
        userService = ac.getBean("userService", UserService.class);
        userDao = ac.getBean("userDao", UserDao.class);
        userDao.deleteAll();

        users.add(new User("userA", "유저A", "passwordA", "emailA@gmail.com", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0));
        users.add(new User("userB", "유저B", "passwordB", "emailB@gmail.com", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0));
        users.add(new User("userC", "유저C", "passwordC", "emailC@gmail.com", Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD-1));
        users.add(new User("userD", "유저D", "passwordD", "emailD@gmail.com", Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD));
        users.add(new User("userE", "유저E", "passwordE", "emailE@gmail.com", Level.GOLD,  100, 100));
    }

    @Test
    @DisplayName("유저 업그레이드 테스트")
    @DirtiesContext
    void testUserLevelUpgrade() throws Exception {
        // given
        users.forEach(user -> userDao.add(user));

        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        // when
        userService.upgradeLevels();

        // then
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    @Test
    @DisplayName("사용자 가입 시 BASIC 레벨 부여")
    void testInitialBasic() {
        // given
        User notInitialUser = users.get(4);
        User initialUser = users.get(0);
        initialUser.setLevel(null);

        // when
        userService.add(notInitialUser);
        userService.add(initialUser);

        User findNotInitialUser = userDao.get(notInitialUser.getId());
        User findInitialUser = userDao.get(initialUser.getId());

        // then
        checkLevel(findNotInitialUser, notInitialUser.getLevel());
        checkLevel(findInitialUser, Level.BASIC);
    }

    private void checkLevel(User user, Level level) {
        User findUser = userDao.get(user.getId());
        assertThat(findUser.getLevel()).isEqualByComparingTo(level);
    }

    private void checkLevelUpgraded(User user, boolean isUpgrad) {
        User findUser = userDao.get(user.getId());
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
        UserService testUserService = new TestUserService(userDao, userLevelUpgradePolicy, platformTransactionManager, mailSender, users.get(3).getId());

        // when

        // then
        assertThrows(TestUserServiceException.class, () -> testUserService.upgradeLevels());
        assertThat(users.get(1).getLevel()).isEqualTo(Level.BASIC);
    }



    private static class TestUserService extends UserService {

        private String id;

        public TestUserService(UserDao userDao, UserLevelUpgradePolicy levelUpgradePolicy, PlatformTransactionManager platformTransactionManager, MailSender mailSender, String id) {
            super(userDao, levelUpgradePolicy, platformTransactionManager, mailSender);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
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
