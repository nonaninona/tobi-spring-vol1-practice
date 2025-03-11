package com.example.chapter5.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.chapter5.v1.upgradeLevelPolicy.DefaultUserLevelUpgradePolicy.*;

import com.example.chapter5.v1.DaoFactory;
import com.example.chapter5.v1.Level;
import com.example.chapter5.v1.User;
import com.example.chapter5.v1.UserDao;
import com.example.chapter5.v1.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setup() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userService = ac.getBean("userService", UserService.class);
        userDao = ac.getBean("userDao", UserDao.class);
        userDao.deleteAll();

        users.add(new User("userA", "유저A", "passwordA", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0));
        users.add(new User("userB", "유저B", "passwordB", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0));
        users.add(new User("userC", "유저C", "passwordC", Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD-1));
        users.add(new User("userD", "유저D", "passwordD", Level.SILVER, 60, MIN_RECOMMENED_COUNT_FOR_GOLD));
        users.add(new User("userE", "유저E", "passwordE", Level.GOLD,  100, 100));
    }

    @Test
    @DisplayName("유저 업그레이드 테스트")
    void testUserLevelUpgrade() {
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

}
