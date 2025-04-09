package com.example.chapter6.v3;

import com.example.chapter6.v3.mailSender.MockMailSender;
import com.example.chapter6.v3.proxy.TransactionHandler;
import com.example.chapter6.v3.upgradeLevelPolicy.UserLevelUpgradePolicy;
import com.example.chapter6.v3.userService.UserService;
import com.example.chapter6.v3.userService.UserServiceImpl;
import com.example.chapter6.v3.userService.UserServiceTx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static com.example.chapter6.v3.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.example.chapter6.v3.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.MIN_RECOMMENED_COUNT_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        userDao.deleteAll();
        users.add(new User("userA", "유저A", "passwordA", "emailA@gmail.com", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0));
        users.add(new User("userB", "유저B", "passwordB", "emailB@gmail.com", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0));
        users.add(new User("userC", "유저C", "passwordC", "emailC@gmail.com", Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD-1));
        users.add(new User("userD", "유저D", "passwordD", "emailD@gmail.com", Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD));
        users.add(new User("userE", "유저E", "passwordE", "emailE@gmail.com", Level.GOLD,  100, 100));
    }

    @Test
    @DisplayName("유저 업그레이드 테스트")
    void testUserLevelUpgrade() {
        // given
        MockUserDao mockUserDao = new MockUserDao(users);
        MockMailSender mockMailSender = new MockMailSender();

        //진짜 고립된 테스트이려면 userLevelUpgradePolicy도 mock 객체 만들어야 함
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao, userLevelUpgradePolicy, mockMailSender);

        // when
        userServiceImpl.upgradeLevels();

        // then
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), "유저B", Level.SILVER);
        checkUserAndLevel(updated.get(1), "유저D", Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    @Test
    @DisplayName("유저 업그레이드 테스트 - 목 프레임워크 사용")
    void testUserLevelUpgradeWithMockito() {
        // given
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(users);

        MailSender mockMailSender = mock(MailSender.class);
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao, userLevelUpgradePolicy, mockMailSender);

        // when
        userServiceImpl.upgradeLevels();

        // then
        verify(mockUserDao, times(2)).update(any(User.class));

        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);

        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArgs = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArgs.capture());
        List<SimpleMailMessage> allValues = mailMessageArgs.getAllValues();
        assertThat(allValues.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(allValues.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User user, String name, Level level) {
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getLevel()).isEqualTo(level);
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
        users.forEach(u -> userDao.add(u));
        UserServiceImpl testUserService = new TestUserService(userDao, userLevelUpgradePolicy, mailSender, users.get(3).getId());

        TransactionHandler txHandler = new TransactionHandler(testUserService, platformTransactionManager, "upgradeLevels");

        UserService userTxService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                txHandler
        );

        // when

        // then
        assertThrows(TestUserServiceException.class, userTxService::upgradeLevels);
        assertThat(users.get(1).getLevel()).isEqualTo(Level.BASIC);
    }



    private static class TestUserService extends UserServiceImpl {

        private String id;

        public TestUserService(UserDao userDao, UserLevelUpgradePolicy levelUpgradePolicy, MailSender mailSender, String id) {
            super(userDao, levelUpgradePolicy, mailSender);
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

    private static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUsers() {
            return users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public int update(User user) {
            updated.add(user);
            return 1;
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void add(User user) {throw new UnsupportedOperationException();}
        @Override
        public User get(String id) {throw new UnsupportedOperationException();}
        @Override
        public void deleteAll() {throw new UnsupportedOperationException();}
        @Override
        public int getCount() {throw new UnsupportedOperationException();}
    }
}
